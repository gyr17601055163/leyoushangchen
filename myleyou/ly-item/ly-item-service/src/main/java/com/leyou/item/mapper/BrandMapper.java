/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: BrandMapper
 * Author:   gyr
 * Date:     2019/3/5 9:07
 * Description:
 * History:
 */
package com.leyou.item.mapper;


import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 添加品牌表和品类表的中间表
     *
     * @param cid 品类的id
     * @param bid 品牌的id
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id,brand_id) VALUES(#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据cid查寻品牌
     * @param cid 品类的id
     * @return
     */
    @Select("SELECT * FROM tb_brand tb LEFT JOIN tb_category_brand tcb ON tcb.brand_id = tb.id WHERE tcb.category_id = #{cid}")
    List<Brand> queryBrandByCid(@Param("cid") Long cid);

}