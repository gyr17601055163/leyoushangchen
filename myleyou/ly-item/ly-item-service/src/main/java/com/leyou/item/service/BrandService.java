/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: BrandService
 * Author:   gyr
 * Date:     2019/3/5 9:08
 * Description:
 */
package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据分页查寻品牌
     *
     * @param page   当前页
     * @param rows   每页显示的条数
     * @param sortBy 根据什么排序
     * @param desc   排序方式
     * @param key    模糊查寻字段
     * @return
     */
    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);
        /**
         * WHERE name LIKE '%X%' OR letter == 'x' ORDER BY id DESC
         */
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {    //判断查寻条件不为空
            //创建过滤条件                            //数据库表中的字段  //查询条件                             //将key（品牌首字母）变成大写
            example.createCriteria().orLike("name", "%" + key + "%").orEqualTo("letter", key.toUpperCase());
        }

        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");    //如果排序方式是true，就倒序，如果是false，就正序
            example.setOrderByClause(orderByClause);
        }

        //查询
        List<Brand> brands = brandMapper.selectByExample(example);//查询所有
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnums.BRAND_NOT_FOND);
        }

        //解析分页结果
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        return new PageResult<>(pageInfo.getTotal(), brands);
    }

    /**
     * 新增品牌
     *
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);  //为了让id为空，可以自增
        int insert = brandMapper.insert(brand); //insertSelective和insert的区别：insertSelective是有选择的添加，如果数据为空，则不添加，只添加非空字段。
        if (insert != 1) {    //说明自增失败
            throw new LyException(ExceptionEnums.BRAND_SAVE_ERROR);
        }

        //新增中间表
        for (Long cid : cids) {
            int i = brandMapper.insertCategoryBrand(cid, brand.getId());
            if(i != 1){
                throw new LyException(ExceptionEnums.BRAND_CATEGORY_SAVE_ERROR);
            }
        }

    }

    /**
     * 通过主键id查寻品牌名称
     * @param id
     * @return
     */
    public Brand queryById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if(brand == null){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }
        return brand;
    }

    /**
     * 根据cid查寻品牌
     * @param cid 品类的id
     * @return
     */
    public List<Brand> queryBrandByCid(Long cid) {
        // 多表关联查需要在mapper层写sql
        List<Brand> brands = brandMapper.queryBrandByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnums.BRAND_NOT_FOND);
        }
        return brands;
    }
}