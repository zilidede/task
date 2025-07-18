package com.zl.utils.other;

/**
 * @className: com.craw.nd.util-> CmdUtils
 * @description: cmd命令行操作
 * @author: zl
 * @createDate: 2023-12-31 16:01
 * @version: 1.0
 * @todo:
 */
public class CmdUtils {
    public static void cmd(String filePath) {
        try {
            // 调用CMD命令
            Runtime.getRuntime().exec(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }
}
