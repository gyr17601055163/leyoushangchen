/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: CategoryClient
 * Author:   gyr
 * Date:     2019/3/15 15:45
 * Description:
 * History:
 */
package com.leyou.search.client;

import com.leyou.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//标识@FeignClient的接口最多只能继承一个接口
//使用@FeignClient注解标识的接口注册成一个 FeignClientFactoryBean 类型的Bean对象
@FeignClient("item-service")
public interface CategoryClient {

    /**
     * 根据商品id查询
     * @param ids
     * @return
     *
     * 使用ResponseEntity和不使用它的区别？
     *  使用：不管状态码是几百，都能成功，不会抛出异常，但是需要我们自己去手动的云判断这个状态码
     *  不使用：状态码在200-300之间的，才会返回成功，其他的都会抛出异常
     */
    @GetMapping("category/list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);




}