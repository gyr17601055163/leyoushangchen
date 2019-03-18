/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: CategoryService
 * Author:   gyr
 * Date:     2019/3/4 23:42
 * Description:
 */
package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        //查询条件，mapper会把对象中的非空属性作为查询条件
        Category t = new Category();
        t.setParentId(pid);
        List<Category> list = categoryMapper.select(t);
        if(CollectionUtils.isEmpty(list)){  //判断list是否为空
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOND);
        }
        return list;
    }

    /**
     * 根据id查看对应的分类
     * @param ids
     * @return
     */
    public List<Category> queryByIds(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }
        return list;
    }

}