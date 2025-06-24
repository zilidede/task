package com.zl.task.keyword;

import com.zl.task.process.keyword.DeepTrendInSightKeywords;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        // 测试空关键字
        String keyword = "";
        Integer deepCount = 2;

        assertThrows(Exception.class, () -> {
            DeepTrendInSightKeywords.crawRootKeyword(keyword, deepCount);
        });
    }

    @Test
    public void testCrawRootKeywordWithNullKeyword() throws Exception {
        // 测试 null 关键字
        String keyword = null;
        Integer deepCount = 2;

        assertThrows(Exception.class, () -> {
            DeepTrendInSightKeywords.crawRootKeyword(keyword, deepCount);
        });
    }

    @Test
    public void testCrawRootKeywordWithNegativeDepth() throws Exception {
        // 测试负深度
        String keyword = "test";
        Integer deepCount = -1;

        // 根据方法逻辑判断是否抛出异常
        assertThrows(Exception.class, () -> {
            DeepTrendInSightKeywords.crawRootKeyword(keyword, deepCount);
        });
    }
}
