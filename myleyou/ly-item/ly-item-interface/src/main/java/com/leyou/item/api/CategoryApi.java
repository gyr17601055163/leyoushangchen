/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: CategoryApi
 * Author:   gyr
 * Date:     2019/3/15 16:55
 * Description:
 * History:
 */
package com.leyou.item.api;


import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {

    @GetMapping("category/list")
    List<Category> queryCategoryListByPid(@RequestParam("pid")Long pid);

    /**
     * 根据id查询商品分类
     * @param ids
     * @return
     */
    @GetMapping("category/list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);
}