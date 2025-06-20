package com.zl.task.process.keyword;

import java.util.List;

public interface Stemmer {
    List<String> stem(List<String> words);
}

