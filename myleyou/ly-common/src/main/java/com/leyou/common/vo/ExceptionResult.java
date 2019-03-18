/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: ExceptionResult
 * Author:   gyr
 * Date:     2019/3/4 9:42
 * Description:
 */
package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnums;
import lombok.Data;

@Data
public class ExceptionResult {
    private  int status;
    private String message;
    private Long timestamp; //时间戳

    public ExceptionResult(ExceptionEnums em){
        this.status = em.getCode();
        this.message = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}