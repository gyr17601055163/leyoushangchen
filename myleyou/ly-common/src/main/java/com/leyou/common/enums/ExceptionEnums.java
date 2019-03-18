/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: ExceptionEnums
 * Author:   gyr
 * Date:     2019/3/4 9:28
 * Description:
 */
package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 异常的枚举
 */
@Getter //只提供get方法
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {


    PRICE_CANNOT_BE_NULL(400, "价格不能为空！"),
    CATEGORY_NOT_FOND(404, "商品分类没有查到！"),
    BRAND_NOT_FOND(404, "品牌不存在！"),
    BRAND_SAVE_ERROR(500, "品牌添加失败"),
    BRAND_CATEGORY_SAVE_ERROR(500, "品牌品类中间表添加失败"),
    UPLOAD_ERROR(500, "图片上传失败"),
    UPLOAD_FILE_TYPE_ERROR(400, "无效的文件类型"),
    SPEC_NOT_FOND(404,"规格不存在！"),
    SPEC_PARAM_NOT_FOND(404,""),
    GOODS_NOT_FOND(404,"商品不存在"),
    GOODS_SAVA_ERROR(500,"新增商品失败"),
    GOODS_UPDATE_ERROR(500,"修改商品失败"),
    GOODS_ID_CANNOT_BE_NULL(400,"商品id不能为空"),
    ;
    private int code;   //状态码
    private String msg;

}