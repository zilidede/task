package com.zl.utils.robot;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @className: com.craw.nd.util.robot-> RobotUtils
 * @description: robot
 * @author: zl
 * @createDate: 2023-12-06 11:21
 * @version: 1.0
 * @todo:
 */
public class RobotUtils {

    private static Robot singleton;

    static {
        try {
            singleton = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  System.out.println("");
    }

    public static Robot getInstance() {
        return singleton;
    }

    public static void keyBoard(String key) {
        if (key.equals("f5")) {
            singleton.keyPress(KeyEvent.VK_F5);
        }
    }
}
