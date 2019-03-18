/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: SpecificationController
 * Author:   gyr
 * Date:     2019/3/6 20:32
 * Description:
 */
package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询规格组
     *
     * @param cid 分类id
     * @return
     */
    @GetMapping("/groups/{cid}")                     ////@PathVariable可以用来映射URL中的占位符到目标方法的参数中
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specificationService.queryGroupByCid(cid));
    }


    /**
     *
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParamList(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching) {
        List<SpecParam> specParams = specificationService.querySpecParamList(gid,cid,searching);
        return ResponseEntity.ok(specParams);
    }


}