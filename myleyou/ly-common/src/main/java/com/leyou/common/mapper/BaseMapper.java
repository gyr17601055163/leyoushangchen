/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: BaseMapper
 * Author:   gyr
 * Date:     2019/3/10 20:23
 * Description:
 */
package com.leyou.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, IdListMapper<T,Long>, InsertListMapper<T> {

}