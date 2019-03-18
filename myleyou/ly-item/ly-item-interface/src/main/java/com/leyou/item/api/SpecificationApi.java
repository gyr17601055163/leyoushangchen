/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: SpecificationApi
 * Author:   gyr
 * Date:     2019/3/15 16:56
 * Description:
 * History:
 */
package com.leyou.item.api;


import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {

    /**
     * 根据分类id查询规格组
     *
     * @param cid 分类id
     * @return
     */
    @GetMapping("/spec/groups/{cid}")                     ////@PathVariable可以用来映射URL中的占位符到目标方法的参数中
    List<SpecGroup> queryGroupByCid(@PathVariable("cid") Long cid);


    /**
     *
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("/spec/params")
    List<SpecParam> querySpecParamList(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching);
}