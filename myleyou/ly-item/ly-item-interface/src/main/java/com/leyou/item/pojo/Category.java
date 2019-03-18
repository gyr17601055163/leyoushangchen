/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: Category
 * Author:   gyr
 * Date:     2019/3/4 23:33
 * Description:
 */
package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_category")
@Data
public class Category {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;

}