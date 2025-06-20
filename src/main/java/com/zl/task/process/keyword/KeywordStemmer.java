package com.zl.task.process.keyword;

import java.util.ArrayList;
import java.util.List;

//测试用例
public class KeywordStemmer {

    public static void main(String[] args) {
        // 示例中文关键词列表
        List<String> keywords = List.of("运行", "跳跃", "更好", "更差", "快乐");

        // 使用IKAnalyzer
        Stemmer ikAnalyzerStemmer = StemmerFactory.getStemmer(StemmerFactory.StemmerType.IK_ANALYZER);
        List<String> ikAnalyzerStems = getStems(keywords, ikAnalyzerStemmer);
        System.out.println("IKAnalyzer Stems: " + ikAnalyzerStems);

        // 使用HanLP
        Stemmer hanLPStemmer = StemmerFactory.getStemmer(StemmerFactory.StemmerType.HANLP);
        List<String> hanLPStems = getStems(keywords, hanLPStemmer);
        System.out.println("HanLP Stems: " + hanLPStems);
    }

    /**
     * 获取关键词列表的词根
     *
     * @param words   关键词列表
     * @param stemmer 词干提取器
     * @return 词根列表
     */
    public static List<String> getStems(List<String> words, Stemmer stemmer) {
        return stemmer.stem(words);
    }
}

