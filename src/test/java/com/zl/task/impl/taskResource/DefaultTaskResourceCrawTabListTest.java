package com.zl.task.impl.taskResource;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import org.junit.Test;

public class DefaultTaskResourceCrawTabListTest {

    @Test
    public void generatorTabs() throws InterruptedException {
        int i = 0;
        DefaultTaskResourceCrawTabList.getTabList().get(i++).get("https://www.baidu.com/s?ie=utf-8&f=3&rsv_bp=1&rsv_idx=1&tn=baidu&wd=%E5%AE%BD%E5%B8%A6%E6%B5%8B%E9%80%9F&fenlei=256&rsv_pq=0xce19dca400c4ad94&rsv_t=ce2deAQJa9f8zv2AEnNQ82PBZ0s6qQtYwpTp5%2FE%2BgoNF1s2MYfMx3QDHSZ%2F%2F&rqlang=en&rsv_dl=ih_0&rsv_enter=1&rsv_sug3=1&rsv_sug1=1&rsv_sug7=001&rsv_sug2=1&rsv_btype=i&rsp=0&rsv_sug9=es_2_1&rsv_sug4=746&rsv_sug=9");
        Thread.sleep(1000);
        // DefaultTaskResourceCrawTabList.getTabList().get(i++).get("https://compass.jinritemai.com/shop/chance/product-rank?first_rank_type=product&second_rank_type=1");
    }

    @Test
    public void getTabList() {
    }
}