/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: BrandApi
 * Author:   gyr
 * Date:     2019/3/15 16:54
 * Description:
 * History:
 */
package com.leyou.item.api;


import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrandApi {

    /**
     * 根据分页查寻品牌
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("brand/page")
    PageResult<Brand> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key);

    /**
     * 新增品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping("brand")
    Void saveBrand(Brand brand, @RequestParam("cids") List<Long> cids);



    /**
     * 根据cid查寻品牌
     * @param cid 品类的id
     * @return
     */
    @GetMapping("brand/cid/{cid}")
    List<Brand> queryBrandByCid(@PathVariable("cid")Long cid);

    /**
     * 根据品牌id查询
     * @param id
     * @return
     */
    @GetMapping("brand/{id}")
    Brand queryBrandById(@PathVariable("id")Long id);

    /**
     * 批量
     * @param ids
     * @return
     */
    @GetMapping("brand/brands")
    List<Brand> queryBrandByIds(@RequestParam("ids")List<Long> ids);
}