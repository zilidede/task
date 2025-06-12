package com.zl.task.save.parser.order;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface SaveOrder {
    void call(String platform, String shop, List<Map<String, String>> maps) throws ParseException;
}
