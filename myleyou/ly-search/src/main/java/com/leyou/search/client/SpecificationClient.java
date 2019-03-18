/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: SpecificationClient
 * Author:   gyr
 * Date:     2019/3/15 16:58
 * Description:
 * History:
 */
package com.leyou.search.client;


import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {

}