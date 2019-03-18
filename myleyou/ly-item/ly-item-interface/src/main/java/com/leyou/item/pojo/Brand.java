/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: Brand
 * Author:   gyr
 * Date:     2019/3/5 9:00
 * Description:
 */
package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_brand")
@Data
public class Brand {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;    //品牌名称
    private String image;   //品牌图片
    private String letter;
}