package com.checksumtool;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ChecksumCalculator {
    // 文件校验和
    private final Map<String, String> fileChecksums;
    // 取消计算标志
    private volatile boolean cancelCalculation;

    // 构造函数
    public ChecksumCalculator() {
        fileChecksums = new HashMap<>();
        cancelCalculation = false;
    }

    // 计算文件的校验和
    public String calculateChecksum(File file, HashAlgorithm algorithm) throws IOException, NoSuchAlgorithmException {
        // 获取MessageDigest实例
        MessageDigest digest = MessageDigest.getInstance(algorithm.getAlgorithmName());
        cancelCalculation = false;

        try (InputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis)) {

            byte[] buffer = new byte[8192];
            int count;
            // 读取文件并更新校验和
            while ((count = bis.read(buffer)) > 0 && !cancelCalculation) {
                digest.update(buffer, 0, count);
            }

            // 如果取消计算，则返回取消计算的信息
            if (cancelCalculation) {
                return "Calculation cancelled";
            }

            // 获取校验和
            byte[] hash = digest.digest();
            // 将校验和转换为十六进制字符串
            return bytesToHex(hash);
        }
    }

    // 将字节数组转换为十六进制字符串
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // 取消计算
    public void cancelCalculation() {
        cancelCalculation = true;
    }

    // 获取文件校验和
    public Map<String, String> getFileChecksums() {
        return fileChecksums;
    }

    // 清空文件校验和
    public void clearChecksums() {
        fileChecksums.clear();
    }

    // 验证算法是否可用
    public static boolean isAlgorithmAvailable(HashAlgorithm algorithm) {
        try {
            MessageDigest.getInstance(algorithm.getAlgorithmName());
            return true;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}
