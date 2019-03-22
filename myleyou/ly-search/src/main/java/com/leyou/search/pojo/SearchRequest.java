/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: SearchRequest
 * Author:   gyr
 * Date:     2019/3/18 11:59
 * Description:
 */
package com.leyou.search.pojo;

import java.util.Map;

/**
 * 用来接收请求的json数据的类
 */
public class SearchRequest {

    private String key;// 搜索条件

    private Integer page;// 当前页

    private Map<String,String> filter;

    private static final Integer DEFAULT_SIZE = 20; // 每页显示的数据条数

    private static final Integer DEFAULT_PAGE = 1;// 默认页

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }
}