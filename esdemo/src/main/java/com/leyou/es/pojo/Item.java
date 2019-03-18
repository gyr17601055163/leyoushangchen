/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: Item
 * Author:   gyr
 * Date:     2019/3/15 9:42
 * Description:
 */
package com.leyou.es.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor // 创建一个有参构造函数
@NoArgsConstructor  // 创建一个无参构造函数
@Document(indexName = "heima3",type = "item",shards = 1)
public class Item {

    @Field(type = FieldType.Long)
    @Id
    Long id;

    @Field(type = FieldType.Text,analyzer = "ik_smart")
    String title; //标题


    @Field(type = FieldType.Keyword)
    private String category;// 分类

    @Field(type = FieldType.Keyword)
    private String brand; // 品牌

    @Field(type = FieldType.Double)
    private Double price; // 价格

    @Field(index = false, type = FieldType.Keyword)
    private String images; // 图片地址

}