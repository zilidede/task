package com.zl.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @className: com.craw.nd.util.io-> FileZipUtils
 * @description: 文件及其文件夹压缩
 * @author: zl
 * @createDate: 2024-01-18 12:22
 * @version: 1.0
 * @todo:
 */
public class FileZipUtils {
    public static void compressFolder(String sourceFolder, String folderName, ZipOutputStream zipOutputStream) throws IOException {
        //参数：
        //sourceFolder：源文件夹的路径（绝对路径或相对路径）。
        //folderName：在 ZIP 文件中表示此文件夹的名称。
        //zipOutputStream：用于写入 ZIP 数据的输出流。
        //功能：
        //创建一个 File 对象表示源文件夹。
        //获取文件夹中的所有文件和子文件夹列表。
        //如果文件夹不为空，则遍历每个文件或子文件夹：
        //如果是子文件夹，递归调用 compressFolder 方法来压缩子文件夹。
        //如果是文件，调用 addToZipFile 方法将文件添加到 ZIP 输出流中。
        File folder = new File(sourceFolder);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 压缩子文件夹
                    compressFolder(file.getAbsolutePath(), folderName + "/" + file.getName(), zipOutputStream);
                } else {
                    // 压缩文件
                    addToZipFile(folderName + "/" + file.getName(), file.getAbsolutePath(), zipOutputStream);
                }
            }
        }
    }

    private static void addToZipFile(String fileName, String fileAbsolutePath, ZipOutputStream zipOutputStream) throws IOException {
        // 创建ZipEntry对象并设置文件名
        ZipEntry entry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(entry);

        // 读取文件内容并写入Zip文件
        try (FileInputStream fileInputStream = new FileInputStream(fileAbsolutePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, bytesRead);
            }
        }

        // 完成当前文件的压缩
        zipOutputStream.closeEntry();
    }
}
