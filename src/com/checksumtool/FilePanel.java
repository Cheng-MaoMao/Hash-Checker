package com.checksumtool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Vector;

// 定义一个文件面板类，继承自JPanel
public class FilePanel extends JPanel {
    // 定义一个下拉框，用于选择哈希算法
    private final JComboBox<HashAlgorithm> algorithmCombo;
    // 定义一个按钮，用于添加文件
    private final JButton addFilesButton;
    // 定义一个按钮，用于计算校验值
    private final JButton calculateButton;
    // 定义一个按钮，用于对比校验值
    private final JButton compareButton;
    // 定义一个按钮，用于清除
    private final JButton clearButton;
    // 定义一个文件列表，用于存储选中的文件
    private final List<File> selectedFiles;

    // 构造函数
    public FilePanel() {
        // 设置布局为左对齐
        setLayout(new FlowLayout(FlowLayout.LEFT));
        // 初始化选中的文件列表
        selectedFiles = new ArrayList<>();
        // 创建一个哈希算法的向量

        Vector<HashAlgorithm> availableAlgorithms = new Vector<>();
        // 遍历所有的哈希算法
        for (HashAlgorithm algorithm : HashAlgorithm.values()) {
            // 如果算法可用，则添加到向量中
            if (ChecksumCalculator.isAlgorithmAvailable(algorithm)) {
                availableAlgorithms.add(algorithm);
            }
        }

        // 初始化下拉框
        algorithmCombo = new JComboBox<>(availableAlgorithms);
        // 设置下拉框的尺寸
        algorithmCombo.setPreferredSize(new Dimension(150, 25));

        // 初始化按钮
        addFilesButton = new JButton("添加文件");
        calculateButton = new JButton("计算校验值");
        compareButton = new JButton("对比校验值");
        clearButton = new JButton("清除");

        // 初始化UI
        initializeUI();
    }

    // 初始化UI
    private void initializeUI() {
        // 创建一个算法面板
        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // 添加一个标签和下拉框到算法面板
        algorithmPanel.add(new JLabel("选择算法："));
        algorithmPanel.add(algorithmCombo);

        // 创建一个按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // 添加按钮到按钮面板
        buttonPanel.add(addFilesButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(compareButton);
        buttonPanel.add(clearButton);

        // 将算法面板和按钮面板添加到文件面板
        add(algorithmPanel);
        add(buttonPanel);
    }

    // 获取下拉框
    public JComboBox<HashAlgorithm> getAlgorithmCombo() {
        return algorithmCombo;
    }

    // 获取添加文件按钮
    public JButton getAddFilesButton() {
        return addFilesButton;
    }

    // 获取计算校验值按钮
    public JButton getCalculateButton() {
        return calculateButton;
    }

    // 获取对比校验值按钮
    public JButton getCompareButton() {
        return compareButton;
    }

    // 获取清除按钮
    public JButton getClearButton() {
        return clearButton;
    }

    // 获取选中的文件列表
    public List<File> getSelectedFiles() {
        return selectedFiles;
    }
}
