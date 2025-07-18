package com.zl.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHashGeneratorUtils {
    // 生成文件哈希值（含元数据）

    public static String generateHashWithMetadata(File file) throws IOException, NoSuchAlgorithmException {
        // 选择哈希算法（如 SHA-256）
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // 1. 更新文件内容
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        // 2. 更新文件元数据（文件名、大小、最后修改时间）
        String metadata = file.getName() + file.length() + file.lastModified();
        digest.update(metadata.getBytes(StandardCharsets.UTF_8));

        // 3. 生成哈希值并转换为十六进制字符串
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        try {
            File file = new File("./data/config/config.ini");
            String hash = generateHashWithMetadata(file);
            System.out.println("文件哈希值（含元数据）: " + hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
