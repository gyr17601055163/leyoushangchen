/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: GoodsApi
 * Author:   gyr
 * Date:     2019/3/15 16:46
 * Description:
 */
package com.leyou.item.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows);

    /**
     * 添加商品
     * @param spu 添加的数据
     * @return
     */
    @PostMapping("/goods")              //通过@requestBody可以将请求体中的JSON字符串绑定到相应的bean上，当然，也可以将其分别绑定到对应的字符串上
    Void saveGoods(@RequestBody Spu spu) ;

    /**
     * 修改商品
     * @param spu
     * @return
     */
    @PutMapping("/goods")
   Void updateGoods(@RequestBody Spu spu);
    /**
     * 根据id查寻spu_Detail的详细信息
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{spuId}")
    SpuDetail queryGoodsById(@PathVariable("spuId") Long spuId);

    /**
     * 根据spu的id查寻商品对应的所有sku信息
     * @param id
     * @return
     */
    @GetMapping("/sku/list")
    List<Sku> querySkuById(@RequestParam("id") Long id);
}