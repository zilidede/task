package com.zl.utils.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

//  执行系统命令 cmd
public class RuntimeUtils {

    public static void killProcessByName(String processName) {
        try {
            // 使用taskkill命令结束进程
            Runtime.getRuntime().exec("taskkill /IM " + processName + ".exe /F");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to kill the process: " + processName);
        }
    }

    // 一个简单的线程类用于读取输入流和错误流
    private static class StreamGobbler implements Runnable {

        private final InputStream inputStream;
        private final Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    public static void exe(String batFilePath) {

        try {
            // 使用Runtime.exec()方法执行bat文件
            Process process = Runtime.getRuntime().exec(batFilePath);

            // 获取进程的输入流，通常用于读取标准输出
            new Thread(new StreamGobbler(process.getInputStream(), System.out::println)).start();

            // 获取进程的错误流，通常用于读取标准错误输出
            new Thread(new StreamGobbler(process.getErrorStream(), System.err::println)).start();

            // 等待进程完成
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}





