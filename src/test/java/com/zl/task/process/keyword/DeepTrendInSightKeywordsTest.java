package com.zl.task.process.keyword;


import org.junit.Test;

public class DeepTrendInSightKeywordsTest {

    @Test
    public void crawRootKeyword() {
        try {
            DeepTrendInSightKeywords.crawRootKeyword("防晒", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}