/**
 * Copyright (C),2018-2019, XXX有限公司
 * FileName: GoodsRepository
 * Author:   gyr
 * Date:     2019/3/16 8:45
 * Description:
 * History:
 */
package com.leyou.search.repository;


import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {

}