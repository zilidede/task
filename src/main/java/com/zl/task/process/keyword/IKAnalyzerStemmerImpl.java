package com.zl.task.process.keyword;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class IKAnalyzerStemmerImpl implements Stemmer {
    @Override
    public List<String> stem(List<String> words) {
        List<String> stems = new ArrayList<>();
        for (String word : words) {
            try {
                IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(word), true);
                Lexeme lexeme;
                while ((lexeme = ikSegmenter.next()) != null) {
                    stems.add(lexeme.getLexemeText());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stems;
    }
}

