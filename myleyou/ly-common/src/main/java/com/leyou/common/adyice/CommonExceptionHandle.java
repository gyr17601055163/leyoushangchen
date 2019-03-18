/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: CommonExceptionHandle
 * Author:   gyr
 * Date:     2019/3/4 9:06
 * Description:
 */
package com.leyou.common.adyice;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //是针对Controller的，在默认情况下，会自动拦截所有的Controller
public class CommonExceptionHandle {

    /**
     * 指明拦截的异常是RuntimeException异常
     * @return
     */
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException e){
        return ResponseEntity.status(e.getExceptionEnums().getCode()).
                body(new ExceptionResult(e.getExceptionEnums()));
    }

}