/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: PageResult
 * Author:   gyr
 * Date:     2019/3/5 9:49
 * Description:
 */
package com.leyou.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private Long total; //总条数
    private Integer totalPage;  //总页数
    private List<T> items;  //当前页数据

    public PageResult(){};

    public PageResult(Long total,List<T> items){
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total,Integer totalPage,List<T> items){
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }

}