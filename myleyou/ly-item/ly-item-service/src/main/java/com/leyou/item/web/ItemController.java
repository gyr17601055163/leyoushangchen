/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: ItemController
 * Author:   gyr
 * Date:     2019/3/4 8:42
 * Description:
 */
package com.leyou.item.web;

import com.leyou.item.pojo.Item;
import com.leyou.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     *
     * @param item
     * @return
     */
    @PostMapping
    //@ResponseBody   //将java对象序列化，放到body里面去
    public ResponseEntity<Item> saveItem(Item item){
        //校验价格
        if(item.getPrice() == null){
            //返回状态码
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        item = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

}