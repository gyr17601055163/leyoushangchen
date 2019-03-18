/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: CategoryMapper
 * Author:   gyr
 * Date:     2019/3/4 23:40
 * Description:
 */
package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {

}