/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: LyGateway
 * Author:   gyr
 * Date:     2019/3/3 20:20
 * Description:
 */
package com.leyou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
public class LyGateway {
    public static void main(String[] args) {
        SpringApplication.run(LyGateway.class);
    }
}