package com.zl.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @Description: 流 iO
 * @Param:
 * @Auther: zl
 * @Date: 2020/3/21 23:40
 */
public class Stream {
    public static void main(String[] args) {

        Stream obj = new Stream();
        obj.printStream();

    }

    public void printStream() {
        OutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.println("123");
        writer.close();
        System.out.println(out);
    }
}
