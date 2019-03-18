package com.leyou.search.repository;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    // 创建索引时用
    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void testCreateIndex(){
        // 创建索引
        template.createIndex(Goods.class);
        // 创建映射
        template.putMapping(Goods.class);
    }

    @Test
    public void loadData(){
        // 查询spu信息
        int page = 1;   // 当前页
        int rows = 100; // 分布单位
        int size = 0;
        do {
            PageResult<Spu> result = goodsClient.querySpuByPage(null, true, page, rows);
            List<Spu> spuList = result.getItems();
            if(CollectionUtils.isEmpty(spuList)){
                break;
            }

            // 构建goods
            List<Goods> goodsList = spuList.stream().map(searchService::buildGoods).collect(Collectors.toList());

            // 存入索引库
            goodsRepository.saveAll(goodsList);

            // 翻页
            page++;
            size = spuList.size();
        }while (size == 100);
    }
}