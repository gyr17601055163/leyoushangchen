/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: StockMapper
 * Author:   gyr
 * Date:     2019/3/10 19:26
 * Description: 库存类Mapper
 * History:
 */
package com.leyou.item.mapper;


import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;    //这个包下的批量新增主键自己设置
import tk.mybatis.mapper.common.Mapper;

                                                    //InsertListMapper<Stock> 批量添加
public interface StockMapper extends BaseMapper<Stock> {

}