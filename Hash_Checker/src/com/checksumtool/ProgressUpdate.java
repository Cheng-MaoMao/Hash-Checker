package com.checksumtool;

// 定义一个名为ProgressUpdate的类，用于更新进度信息
public class ProgressUpdate {
    // 定义一个整型变量progress，用于存储进度信息
    private final int progress;
    // 定义一个字符串变量fileName，用于存储文件名信息
    private final String fileName;
    // 定义一个字符串变量checksumInfo，用于存储校验和信息
    private final String checksumInfo;

    // 构造函数，用于初始化ProgressUpdate对象
    public ProgressUpdate(int progress, String fileName, String checksumInfo) {
        this.progress = progress;
        this.fileName = fileName;
        this.checksumInfo = checksumInfo;
    }

    // 获取进度信息
    public int getProgress() {
        return progress;
    }

    // 获取文件名信息
    public String getFileName() {
        return fileName;
    }

    // 获取校验和信息
    public String getChecksumInfo() {
        return checksumInfo;
    }
}
