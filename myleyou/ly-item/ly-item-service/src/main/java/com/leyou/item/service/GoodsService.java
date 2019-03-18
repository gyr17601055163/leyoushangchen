/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: GoodsService
 * Author:   gyr
 * Date:     2019/3/8 11:30
 * Description:
 */
package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.reflect.misc.ConstructorUtil;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;


    /**
     * 分页查寻SPU
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    public PageResult<Spu> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        // 分页
        PageHelper.startPage(page, rows);

        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        // 查寻条件
        if (StringUtils.isNotBlank(key)) {        // 不为空
            criteria.andLike("title", "%" + key + "%");
        }

        // 上架下架
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        // 默认排序
        example.setOrderByClause("last_update_time DESC");

        // 查询
        List<Spu> list = spuMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }

        // 解析分类和品牌的名称
        loadCategoryAndBrandName(list);

        // 解析分页结果
        PageInfo<Spu> spuPageInfo = new PageInfo<>(list);

        return new PageResult<>(spuPageInfo.getTotal(), list);
    }

    /**
     * 解析分类和品牌的名称
     *
     * @param list
     */
    private void loadCategoryAndBrandName(List<Spu> list) {

        for (Spu spu : list) {
            // 处理分类名称
            List<String> stringStream = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(category -> category.getName()).collect(Collectors.toList());
            spu.setCname(StringUtils.join(stringStream, "/"));

            // 处理品牌名称
            Brand brand = brandService.queryById(spu.getBrandId());
            spu.setBname(brand.getName());
        }

    }

    /**
     * 添加商品
     *
     * @param spu
     */
    @Transactional
    public void saveGoods(Spu spu) {
        // 添加tb_spu表
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        spu.setSaleable(true);      //默认上架
        spu.setValid(false);        //默认不删除
        int count = spuMapper.insert(spu);
        if (count <= 0) {
            throw new LyException(ExceptionEnums.GOODS_SAVA_ERROR);
        }

        // 添加tb_spu_detail表
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        count = spuDetailMapper.insert(spuDetail);
        if (count <= 0) {
            throw new LyException(ExceptionEnums.GOODS_SAVA_ERROR);
        }

        // 添加sku和sotck
        saveSkuAndStock(spu);
    }

    /**
     * 添加sku和sotck
     * @param spu
     */
    @Transactional
    private void saveSkuAndStock(Spu spu) {
        int count;
        Stock stock = null;
        List<Sku> skus = spu.getSkus();
        List<Stock> stocks = new ArrayList<>();
        for (Sku sku : skus) {
            // 添加tb_sku表
            sku.setId(null);
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            count = skuMapper.insert(sku);
            if (count <= 0) {
                throw new LyException(ExceptionEnums.GOODS_SAVA_ERROR);
            }

            // 添加tb_stock表
            stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);

            //stockMapper.insertSelective(stock);
        }

        // 批量添加tb_stock表
        count = stockMapper.insertList(stocks);
        if (count != stocks.size()) {
            throw new LyException(ExceptionEnums.GOODS_SAVA_ERROR);
        }
    }

    /**
     * id查寻spu_Detail的详细信息
     * @param spuId
     * @return
     */
    public SpuDetail queryGoodsById(Long spuId) {
        // 根据主键id查寻spu
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);

        if(spuDetail == null){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }

        return spuDetail;
    }

    /**
     * 根据spu的id查寻商品对应的所有sku信息
     * @param id
     * @return
     */
    public List<Sku> querySkuById(Long id) {

        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",id);
        List<Sku> skus = skuMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }

        //查寻库存
        for (Sku sku : skus) {
            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
            if(stock == null){
                throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
            }
            sku.setStock(stock.getStock());
        }

        return skus;
    }

    /**
     * 修改商品
     * @param spu
     */
    @Transactional
    public void updateGoods(Spu spu) {
        if(spu.getId() == null){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        // 查寻sku
        List<Sku> skuList = skuMapper.select(sku);
        if(!CollectionUtils.isEmpty(skuList)){
            // 删除sku
            skuMapper.delete(sku);

            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());

            // 批量删除stock
            stockMapper.deleteByIdList(ids);
        }

        // 修改spu
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);

        /**
         * updateByPrimaryKey对你注入的字段全部更新
         * updateByPrimaryKeySelective会对字段进行判断再更新(如果为Null就忽略更新)，如果你只想更新某一字段，可以用这个方法
         */
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if(count != 1){
            throw new LyException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }

        // 修改detail
        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(count != 1){
            throw new LyException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }

        // 新增sku和stock
        saveSkuAndStock(spu);
    }
}