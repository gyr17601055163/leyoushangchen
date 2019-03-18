/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: GoodsClient
 * Author:   gyr
 * Date:     2019/3/15 16:49
 * Description:
 * History:
 */
package com.leyou.search.client;


import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {

}