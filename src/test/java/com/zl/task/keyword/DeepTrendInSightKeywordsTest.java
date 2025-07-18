package com.zl.task.keyword;

import com.zl.task.process.keyword.DeepTrendInSightKeywords;
import org.junit.Test;



public class DeepTrendInSightKeywordsTest {

    @Test
    public void testCrawRootKeywordWithValidInput() throws Exception {
        // 测试正常输入
        String keyword = "防晒衣";
        Integer deepCount = 1;
        // 调用方法
        DeepTrendInSightKeywords.crawRootKeyword(keyword, deepCount);

        // 可以进一步验证 HttpVO 的结果是否符合预期
        // List<HttpVO> result = ... 获取实际结果
        // assertNotNull(result);
    }

    @Test
    public void testCrawRootKeywordWithEmptyKeyword() throws Exception {

    }

    @Test
    public void testCrawRootKeywordWithNullKeyword() throws Exception {

    }

    @Test
    public void testCrawRootKeywordWithNegativeDepth() throws Exception {

    }
}
