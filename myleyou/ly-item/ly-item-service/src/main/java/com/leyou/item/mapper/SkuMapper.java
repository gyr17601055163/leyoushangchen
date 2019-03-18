/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: SkuMapper
 * Author:   gyr
 * Date:     2019/3/8 11:28
 * Description:
 * History:
 */
package com.leyou.item.mapper;


import com.leyou.item.pojo.Sku;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface SkuMapper extends Mapper<Sku> , InsertListMapper<Sku> {

}