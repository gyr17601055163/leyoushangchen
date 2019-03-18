/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: SearchService
 * Author:   gyr
 * Date:     2019/3/16 9:02
 * Description:
 */
package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 把一个spu构建成一个goods
     *
     * @param spu
     * @return
     */
    public Goods buildGoods(Spu spu) {
        Long spuId = spu.getId();
        // 查询分类
        List<Category> categoryList = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> namse = categoryList.stream().map(Category::getName).collect(Collectors.toList());
        // 查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        // 搜索字段             标题，                 分类，品牌，规格
        String all = spu.getTitle() + StringUtils.join(namse, " ") + brand.getName();

        // 所有sku的价格集合
        List<Sku> skuList = goodsClient.querySkuById(spu.getId());
        Set<Long> priceList = new HashSet<>();

        // 所有的sku的集合的json格式
        List<Map<String, Object>> skus = new ArrayList<>();
        skuList.forEach(sku -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            skus.add(map);
            // 处理价格
            priceList.add(sku.getPrice());
        });

        // 查询规格参数
        List<SpecParam> params = specClient.querySpecParamList(null, spu.getCid3(), true);
        // 查询商品详情
        SpuDetail spuDetail = goodsClient.queryGoodsById(spuId);
        // 获取通用规格参数
        String json = spuDetail.getGenericSpec();
        // json格式的数据不好往出取对应的值，所以将json串转成map类型
        Map<String, String> genericSpecMap = JsonUtils.toMap(json, String.class, String.class);
        // 获取特有规格参数
        String json1 = spuDetail.getSpecialSpec();
        Map<String, List<String>> specialSpecMap = JsonUtils.nativeRead(json1, new TypeReference<Map<String, List<String>>>() {
        });

        // 规格参数  key是规格参数的名字，值是规格参数的值
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            // 规格名称
            String key = param.getName();
            // 规格的值
            Object value = "";
            // 判断是否是通用规格
            if (param.getGeneric()) { // 是通用规格
                value = genericSpecMap.get(param.getId().toString());
                // 判断是否是数值类型
                if (param.getNumeric()) {
                    // 处理分段
                    value = chooseSegment(value.toString(), param);
                }
            } else {  // 不是通用规格
                value = specialSpecMap.get(param.getId().toString());
            }
            // 存入map
            specs.put(key, value);
        }

        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setAll(all);   // 搜索字段，包含标题，分类，品牌，规格等
        goods.setPrice(priceList);   // 所有sku的价格集合
        goods.setSkus(JsonUtils.toString(skus));    // 所有的sku的集合的json格式
        goods.setSpecs(specs);   // 所有的可搜索的规格参数
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }

    /**
     * 存入时要进行处理
     *
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        // 将传进来的值转为double类型
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);  // 开始
            double end = Double.MAX_VALUE;                  // 结束
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 搜索商品
     * @param request
     * @return
     */
    public PageResult<Goods> search(SearchRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            return null;
        }
        // 创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 1、结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));

        // 2、分页
        queryBuilder.withPageable(PageRequest.of(page, size));

        // 3、过滤
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", key));

        // 4、查询
        Page<Goods> result = goodsRepository.search(queryBuilder.build());

        // 5、解析结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Goods> goodsList = result.getContent();

        return new PageResult<>(total, totalPage, goodsList);
    }
}