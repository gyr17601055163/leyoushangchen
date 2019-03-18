/**
 * Copyright (C), 2018-2019, TODO有限公司
 * FileName: ItemRepository
 * Author:   gyr
 * Date:     2019/3/15 11:18
 * Description:
 */
package com.leyou.es.pojo;

import com.vividsolutions.jts.geom.LineString;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item, Long> {

    /**
     * 根据价格的范围查寻
     * @param begin
     * @param end
     * @return
     */
    List<Item> findByPriceBetween(Double begin,Double end);
}