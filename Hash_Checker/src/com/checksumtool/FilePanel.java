package com.checksumtool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class FilePanel extends JPanel {
    // 定义算法下拉框
    private final JComboBox<String> algorithmCombo;
    // 定义添加文件按钮
    private final JButton addFilesButton;
    // 定义计算校验值按钮
    private final JButton calculateButton;
    // 定义对比校验值按钮
    private final JButton compareButton;
    // 定义清除按钮
    private final JButton clearButton;
    // 定义已选择的文件列表
    private final List<File> selectedFiles;

    public FilePanel() {
        // 设置布局为左对齐
        setLayout(new FlowLayout(FlowLayout.LEFT));
        // 初始化已选择的文件列表
        selectedFiles = new ArrayList<>();

        // 初始化算法下拉框
        algorithmCombo = new JComboBox<>(new String[] { "MD5", "SHA-256" });
        // 初始化添加文件按钮
        addFilesButton = new JButton("添加文件");
        // 初始化计算校验值按钮
        calculateButton = new JButton("计算校验值");
        // 初始化对比校验值按钮
        compareButton = new JButton("对比校验值");
        // 初始化清除按钮
        clearButton = new JButton("清除");

        // 初始化UI
        initializeUI();
    }

    // 初始化UI
    private void initializeUI() {
        // 添加标签
        add(new JLabel("选择算法："));
        // 添加算法下拉框
        add(algorithmCombo);
        add(addFilesButton);
        add(calculateButton);
        add(compareButton);
        add(clearButton);
    }

    // Getter
    public JComboBox<String> getAlgorithmCombo() {
        return algorithmCombo;
    }

    public JButton getAddFilesButton() {
        return addFilesButton;
    }

    public JButton getCalculateButton() {
        return calculateButton;
    }

    public JButton getCompareButton() {
        return compareButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }
}
