package com.checksumtool;

import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class ChecksumCalculator {
    // 定义一个Map，用于存储文件的校验和
    private final Map<String, String> fileChecksums;

    // 构造函数，初始化Map
    public ChecksumCalculator() {
        fileChecksums = new HashMap<>();
    }

    // 计算文件的校验和
    public String calculateChecksum(File file, String algorithm) {
        try {
            // 获取MessageDigest实例
            MessageDigest digest = MessageDigest.getInstance(
                    algorithm.equals("MD5") ? "MD5" : "SHA-256");

            // 使用BufferedInputStream读取文件
            try (InputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis)) {

                // 定义一个缓冲区，用于存储读取的数据
                byte[] buffer = new byte[8192];
                int count;
                // 循环读取文件数据，并更新MessageDigest
                while ((count = bis.read(buffer)) > 0) {
                    digest.update(buffer, 0, count);
                }

                // 获取文件的校验和
                byte[] hash = digest.digest();
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1)
                        hexString.append('0');
                    hexString.append(hex);
                }
                return hexString.toString();
            }
        } catch (Exception e) {
            // 如果发生异常，返回错误信息
            return "Error: " + e.getMessage();
        }
    }

    // 获取文件的校验和Map
    public Map<String, String> getFileChecksums() {
        return fileChecksums;
    }

    // 清空文件的校验和Map
    public void clearChecksums() {
        fileChecksums.clear();
    }
}
