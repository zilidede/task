package com.zl.utils.flv;


import com.zl.utils.io.FileIoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @className: com.craw.nd.util.flv-> FlvUtils
 * @description: flv 文件工具类
 * @author: zl
 * @createDate: 2024-02-01 12:04
 * @version: 1.0
 * @todo:
 */
public class FlvUtils {
    static class SpiltFile {
        String outputFilePath;
        String startTime;
        String duration;

    }

    public static void singleSpilt(String inputFile, String outputFile, int startTime, int duration) {
        //单文件分割
        FileIoUtils.deteleFile(outputFile);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", inputFile, "-ss", String.valueOf(startTime), "-t", String.valueOf(duration), "-c", "copy", outputFile);
            Process process = processBuilder.start();
            dealStream(process);
            process.waitFor(); // 等待进程完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error occurred while splitting the FLV file.");
            } else {
                System.out.println("FLV file successfully split.");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public static int multiSpilt(String inputFilePath, List<SpiltFile> list) {
        String ffmpegPath = "ffmpeg"; // 确保ffmpeg可执行文件在你的系统路径中
        ProcessBuilder processBuilder = new ProcessBuilder(ffmpegPath, "-i", inputFilePath);
        for (SpiltFile spiltFile : list) {
            processBuilder.command().add("-ss");
            processBuilder.command().add(spiltFile.startTime); // 起始时间
            processBuilder.command().add("-t");
            processBuilder.command().add(spiltFile.duration); // 持续时间
            processBuilder.command().add("-c");
            processBuilder.command().add("copy"); // 拷贝
            processBuilder.command().add(spiltFile.outputFilePath); // 输出文件路径
                    /*
                      processBuilder.command().add("00:00:10"); // 起始时间
                      processBuilder.command().add("-t");
                      processBuilder.command().add("00:00:05"); // 持续时间
                     */
        }
        try {
            Process process = processBuilder.start();
            dealStream(process); //防止阻塞
            return process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        String inputFile = "D:\\work\\nd\\工作日志\\2024-01-31\\录屏\\20240131_131812.flv";
        String outputFile = "./data/flv/output.flv";
        int startTime = 150; // 指定开始时间，单位为秒
        int duration = 2000; // 指定时长，单位为秒
        FileIoUtils.deteleFile(outputFile);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", inputFile, "-ss", String.valueOf(startTime), "-t", String.valueOf(duration), "-c", "copy", outputFile);
            Process process = processBuilder.start();
            dealStream(process);
            process.waitFor(); // 等待进程完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error occurred while splitting the FLV file.");
            } else {
                System.out.println("FLV file successfully split.");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void dealStream(Process process) {
        if (process == null) {
            return;
        }
        // 处理InputStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = in.readLine()) != null) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        // 处理ErrorStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                try {
                    while ((line = err.readLine()) != null) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        err.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
