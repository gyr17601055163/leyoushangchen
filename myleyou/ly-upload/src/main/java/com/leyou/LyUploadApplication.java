/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: LyUploadApplication
 * Author:   gyr
 * Date:     2019/3/5 16:53
 * Description:
 */
package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LyUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyUploadApplication.class);
    }
}