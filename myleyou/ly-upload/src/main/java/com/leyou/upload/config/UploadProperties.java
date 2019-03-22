/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: UploadProperties
 * Author:   gyr
 * Date:     2019/3/19 19:51
 * Description:
 */
package com.leyou.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "ly.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;
}