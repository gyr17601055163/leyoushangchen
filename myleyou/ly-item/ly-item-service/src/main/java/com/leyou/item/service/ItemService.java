/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: ItemService
 * Author:   gyr
 * Date:     2019/3/4 8:40
 * Description:
 */
package com.leyou.item.service;

import com.leyou.item.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ItemService {

    public Item saveItem(Item item){
        //随机生成id
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}