package com.leyou.search.client;

import com.leyou.item.pojo.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void queryCategoryByIds() {
        List<Category> list = categoryClient.queryCategoryByIds(Arrays.asList(1L, 2L, 3L));
        // 断言              断言能查出3条，实际查出的数据条数
        Assert.assertEquals(3, list.size());        // 如果不是3个就报错
        for (Category category : list) {
            System.out.println("category = " + category);
        }
    }
}