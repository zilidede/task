package com.zl.task.save;

import org.junit.Test;

public class SaverTest {

    @Test
    public void save() {
        try {
            Saver.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}