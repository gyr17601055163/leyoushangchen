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
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
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

@Slf4j
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

    @Autowired
    private ElasticsearchTemplate template;

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
     *
     * @param request
     * @return
     */
    public PageResult<Goods> search(SearchRequest request) {

        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            return null;
        }

        int page = request.getPage() - 1;
        int size = request.getSize();

        // 创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 1、结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));

        // 2、分页
        queryBuilder.withPageable(PageRequest.of(page, size));

        // 3、过滤,搜索条件
        //QueryBuilder basicQuery = QueryBuilders.matchQuery("all", key);
        QueryBuilder basicQuery = buildBasicQuery(request);
        queryBuilder.withQuery(basicQuery);

        // 4 聚合分类和品牌
        // 4.1 聚合分类
        String categoryAggName = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        // 4.2 聚合品牌
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        // 5、查询
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);

        // 6、解析结果
        // 6.1 解析分页结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        // 6.2 解析聚合结果
        Aggregations aggs = result.getAggregations();
        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));

        // 7、根据商品分类判断是否需要聚合
        List<Map<String, Object>> specs = null;
        if (categories != null && categories.size() == 1) {
            // 如果商品分类只有一个 才进行聚合，并根据分类与基本查询条件聚合
            specs = getSpec(categories.get(0).getId(), basicQuery);
        }

        return new SearchResult(total, totalPage, goodsList, categories, brands, specs);
    }

    private QueryBuilder buildBasicQuery(SearchRequest request) {
        // 创建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()));
        // 过滤条件
        Map<String, String> map = request.getFilter();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            // 处理key
            if (!"cid3".equals(key) && !"brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }
            String value = entry.getValue();
            queryBuilder.filter(QueryBuilders.termQuery(key, value));
        }
        return queryBuilder;
    }

    /**
     * 聚合出规格参数
     *
     * @param cid
     * @param query
     * @return
     */
    private List<Map<String, Object>> getSpec(Long cid, QueryBuilder query) {

        try {
            List<Map<String, Object>> specs = new ArrayList<>();
            // 不管是全局参数还是sku参数，只要是搜索参数，都根据分类id查询出来
            List<SpecParam> params = specClient.querySpecParamList(null, cid, true);//这里我们需要根据id查询规格，而规格参数接口需要从商品微服务提供
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(query);

            // 聚合规格参数
            params.forEach(p -> {
                String key = p.getName();
                // 因为规格参数保存时不做分词，因此其名称会自动带上一个.keyword后缀
                queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs." + key + ".keyword"));
            });

            // 查询
            Map<String, Aggregation> aggs = template.query(queryBuilder.build(), SearchResponse::getAggregations).asMap();

            // 解析聚合结果
            params.forEach(param -> {
                Map<String, Object> spec = new HashMap<>();
                String key = param.getName();
                spec.put("k", key);
                StringTerms terms = (StringTerms) aggs.get(key);
                spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
                specs.add(spec);
            });

            return specs;
        } catch (Exception e) {
            log.error("【搜索服务】规格聚合出现异常", e);
            return null;
        }
    }

    /**
     * 解析品牌聚合结果
     *
     * @param terms
     * @return
     */
    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());

            List<Brand> brands = brandClient.queryBrandByIds(ids);
            return brands;
        } catch (Exception e) {
            log.error("【搜索服务】查寻品牌异常", e);
            return null;
        }
    }

    /**
     * 解析商品分类聚合结果
     *
     * @param terms
     * @return
     */
    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());

            List<Category> categories = categoryClient.queryCategoryByIds(ids);
            return categories;
        } catch (Exception e) {
            log.error("【搜索服务】查寻分类异常", e);
            return null;
        }
    }
}