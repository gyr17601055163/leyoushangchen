/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: GoodController
 * Author:   gyr
 * Date:     2019/3/8 9:43
 * Description:商品管理控制器
 */
package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GoodController {

    @Autowired
    private GoodsService goodService;

    /**
     * 分页查寻SPU
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows) {
        return ResponseEntity.ok(goodService.querySpuByPage(key, saleable, page, rows));
    }

    /**
     * 添加商品
     * @param spu 添加的数据
     * @return
     */
    @PostMapping("/goods")              //通过@requestBody可以将请求体中的JSON字符串绑定到相应的bean上，当然，也可以将其分别绑定到对应的字符串上
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
        goodService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * PUT和POS都有更改指定URI的语义.但PUT被定义为idempotent的方法，POST则不是.
     * idempotent的方法:如果一个方法重复执行多次，产生的效果是一样的，那就是idempotent的。也就是说：
     *
     *
     * PUT请求：如果两个请求相同，后一个请求会把第一个请求覆盖掉。（所以PUT用来改资源）
     * Post请求：后一个请求不会把第一个请求覆盖掉
     */

    /**
     * 修改商品
     * @param spu
     * @return
     */
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();       //405
    }

    /**
     * 根据id查寻spu_Detail的详细信息
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> queryGoodsById(@PathVariable("spuId") Long spuId) {
        return ResponseEntity.ok(goodService.queryGoodsById(spuId));
    }

    /**
     * 根据spu的id查寻商品对应的所有sku信息
     * @param id
     * @return
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(goodService.querySkuById(id));
    }
}