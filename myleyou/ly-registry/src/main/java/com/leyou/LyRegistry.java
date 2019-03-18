/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: LyRegistry
 * Author:   gyr
 * Date:     2019/3/3 20:10
 * Description:
 */
package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class LyRegistry {
    public static void main(String[] args) {
        SpringApplication.run(LyRegistry.class);
    }
}