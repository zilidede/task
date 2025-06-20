package com.zl.utils.io;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class LatestCreatedFileFinderUtils {

    /**
     * 从文件路径列表中找到创建时间最晚的文件。
     *
     * @param filePaths 文件路径列表
     * @return 包含文件名和创建时间的Optional对象，如果没有文件则返回空Optional
     */
    public static Optional<FileWithCreationTime> findLatestCreatedFile(List<String> filePaths) {
        return filePaths.stream()
                .map(filePath -> {
                    try {
                        Path path = Paths.get(filePath);
                        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                        return new FileWithCreationTime(path.getFileName().toString(), attrs.creationTime().toMillis());
                    } catch (IOException e) {
                        System.err.println("Error accessing file: " + filePath + " - " + e.getMessage());
                        return null; // 或者你可以选择抛出异常或跳过该文件
                    }
                })
                .filter(fileInfo -> fileInfo != null) // 移除任何可能的null值（例如由于IO错误）
                .max((file1, file2) -> Long.compare(file1.creationTime, file2.creationTime)); // 找到创建时间最晚的文件
    }

    /**
     * 用于保存文件名和创建时间的辅助类。
     */
    public static class FileWithCreationTime {
        private final String fileName;
        private final long creationTime;

        public FileWithCreationTime(String fileName, long creationTime) {
            this.fileName = fileName;
            this.creationTime = creationTime;
        }

        public String getFileName() {
            return fileName;
        }

        public long getCreationTime() {
            return creationTime;
        }

        @Override
        public String toString() {
            return "File name: " + fileName + ", Created at: " + Instant.ofEpochMilli(creationTime);
        }
    }

    public static void main(String[] args) {
        // 示例文件路径列表
        List<String> filePaths = DiskIoUtils.listFilesByExtension("d:\\", "csv");

        // 查找创建时间最晚的文件
        Optional<FileWithCreationTime> latestFile = findLatestCreatedFile(filePaths);

        // 打印结果
        latestFile.ifPresentOrElse(
                fileWithCreationTime -> System.out.println(fileWithCreationTime),
                () -> System.out.println("No files found or an error occurred.")
        );
    }
}