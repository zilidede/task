package com.zl.utils.io;

import junit.framework.TestCase;

import java.util.List;

import static com.zl.utils.io.DiskIoUtils.listFilesByExtension;

public class DiskIoTest extends TestCase {

    public void testListFilesByExtension() {
        List<String> txtFiles = listFilesByExtension("D:\\", ".csv");
        txtFiles.forEach(System.out::println);

        // 示例调用：列出"C:\\path\\to\\directory"目录下所有的文件
        List<String> allFiles = listFilesByExtension("D:\\", "*");
        allFiles.forEach(System.out::println);
    }
}