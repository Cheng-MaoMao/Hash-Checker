package com.checksumtool;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.LongConsumer;

public class ChecksumCalculator {
    // 定义一个Map，用于存储文件的校验和
    private final Map<String, String> fileChecksums;
    // 定义一个volatile变量，用于控制计算校验和的取消
    private volatile boolean cancelCalculation;

    // 构造函数，初始化Map和volatile变量
    public ChecksumCalculator() {
        fileChecksums = new HashMap<>();
        cancelCalculation = false;
    }

    // 计算文件的校验和，不提供进度回调
    public String calculateChecksum(File file, HashAlgorithm algorithm) throws IOException, NoSuchAlgorithmException {
        return calculateChecksum(file, algorithm, null);
    }

    // 计算文件的校验和，提供进度回调
    public String calculateChecksum(File file, HashAlgorithm algorithm, LongConsumer progressCallback)
            throws IOException, NoSuchAlgorithmException {
        // 获取MessageDigest实例
        MessageDigest digest = MessageDigest.getInstance(algorithm.getAlgorithmName());
        // 重置volatile变量
        cancelCalculation = false;

        try (InputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis)) {

            // 定义缓冲区
            byte[] buffer = new byte[8192];
            int count;
            long totalBytesRead = 0;

            // 循环读取文件，直到文件末尾或取消计算
            while ((count = bis.read(buffer)) > 0 && !cancelCalculation) {
                // 更新MessageDigest
                digest.update(buffer, 0, count);
                // 累加已读取的字节数
                totalBytesRead += count;

                // 如果提供了进度回调，则调用回调函数
                if (progressCallback != null) {
                    progressCallback.accept(count);
                }
            }

            // 如果取消了计算，则返回取消信息
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

    // 取消计算校验和
    public void cancelCalculation() {
        cancelCalculation = true;
    }

    // 获取文件的校验和
    public Map<String, String> getFileChecksums() {
        return fileChecksums;
    }

    // 清空文件的校验和
    public void clearChecksums() {
        fileChecksums.clear();
    }

    // 判断指定的校验和算法是否可用
    public static boolean isAlgorithmAvailable(HashAlgorithm algorithm) {
        try {
            // 获取MessageDigest实例
            MessageDigest.getInstance(algorithm.getAlgorithmName());
            return true;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}
