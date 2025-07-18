package com.zl.task.process.keyword;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.util.ArrayList;
import java.util.List;

public class HanLPStemmerImpl implements Stemmer {
    @Override
    public List<String> stem(List<String> words) {
        List<String> stems = new ArrayList<>();
        for (String word : words) {
            List<Term> termList = StandardTokenizer.segment(word);
            for (Term term : termList) {
                if (term.length() >= 2)
                    stems.add(term.word);
            }
        }
        return stems;
    }
}

