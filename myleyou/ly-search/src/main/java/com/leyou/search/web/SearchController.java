/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: SearchController
 * Author:   gyr
 * Date:     2019/3/18 13:58
 * Description:
 */
package com.leyou.search.web;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 搜索商品
     * @param request
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request){
        PageResult<Goods> search = searchService.search(request);
        if(search == null){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }
        return ResponseEntity.ok(search);
    }
}