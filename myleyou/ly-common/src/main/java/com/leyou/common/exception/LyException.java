/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: LyException
 * Author:   gyr
 * Date:     2019/3/4 9:27
 * Description:
 */
package com.leyou.common.exception;

import com.leyou.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LyException extends RuntimeException{

    private ExceptionEnums exceptionEnums;

}