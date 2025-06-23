package com.zl.task.process.keyword;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeepTrendInSightKeywordsTest {

    @Test
    void crawRootKeyword() {
        try {
            DeepTrendInSightKeywords.crawRootKeyword("防晒衣", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}