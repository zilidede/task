package com.zl.utils.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class NonBlockingBatExecution {

    public static void exe(String batFilePath) {
        try {
            // 使用Runtime.exec()方法执行bat文件
            Process process = Runtime.getRuntime().exec(batFilePath);

            // 创建一个线程池来处理输入流和错误流
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            // 提交任务来读取输入流
            executorService.submit(new StreamGobbler(process.getInputStream(), System.out::println));

            // 提交任务来读取错误流
            executorService.submit(new StreamGobbler(process.getErrorStream(), System.err::println));

            // 关闭线程池
            executorService.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
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
}