/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: CategoryController
 * Author:   gyr
 * Date:     2019/3/4 23:44
 * Description:
 */
package com.leyou.item.web;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid")Long pid){
        System.out.println("===");
        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }

    /**
     * 根据id查询商品分类
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids){
        List<Category> list = this.categoryService.queryByIds(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }
        return ResponseEntity.ok(list);
    }

}