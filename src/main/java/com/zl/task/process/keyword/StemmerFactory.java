package com.zl.task.process.keyword;

public class StemmerFactory {
    public enum StemmerType {
        IK_ANALYZER,
        HANLP
    }

    public static Stemmer getStemmer(StemmerType type) {
        switch (type) {
            case IK_ANALYZER:
                return new IKAnalyzerStemmerImpl();
            case HANLP:
                return new HanLPStemmerImpl();
            default:
                throw new IllegalArgumentException("Unknown stemmer type: " + type);
        }
    }
}

