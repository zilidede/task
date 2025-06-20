package com.zl.utils.uuid;


import ch.qos.logback.core.testUtil.TeeOutputStream;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class UUIDGeneratorUtils {

    public static void main(String[] args) {
        String uuid = generateCustomUUID();
        System.out.println("Generated UUID: " + uuid);
    }

    public static String generateCustomUUID() {
        try {
            // 获取当前时间戳
            long timestamp = Instant.now().toEpochMilli();

            // 获取机器名并转换为字节数组
            String machineName = InetAddress.getLocalHost().getHostName();
            byte[] machineNameBytes = machineName.getBytes();

            // 使用SHA-256对机器名进行哈希处理
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(machineNameBytes);

            // 只取哈希结果的前几个字节，这里我们取前8个字节
            byte[] hashPart = Arrays.copyOfRange(hash, 0, 8);

            // 将时间戳和机器名哈希组合
            byte[] combinedData = new byte[16];
            TeeOutputStream Longs = null;
            //  System.arraycopy(Longs.toByteArray(timestamp), 0, combinedData, 0, 8);
            System.arraycopy(hashPart, 0, combinedData, 8, 8);

            // 转换为UUID格式
            long msb = 0;
            long lsb = 0;
            for (int i = 0; i < 8; i++) {
                if (i < 4) {
                    msb = (msb << 8) | (combinedData[i] & 0xff);
                } else {
                    lsb = (lsb << 8) | (combinedData[i] & 0xff);
                }
            }
            for (int i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (combinedData[i] & 0xff);
            }

            return UUID.nameUUIDFromBytes(combinedData).toString();
        } catch (NoSuchAlgorithmException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
