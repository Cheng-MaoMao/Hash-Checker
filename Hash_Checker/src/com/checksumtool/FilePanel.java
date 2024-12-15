package com.checksumtool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Vector;

public class FilePanel extends JPanel {
    // 哈希算法选择框
    private final JComboBox<HashAlgorithm> algorithmCombo;
    // 添加文件按钮
    private final JButton addFilesButton;
    // 计算校验值按钮
    private final JButton calculateButton;
    // 对比校验值按钮
    private final JButton compareButton;
    // 清除按钮
    private final JButton clearButton;
    // 取消计算按钮
    private final JButton cancelButton;
    // 已选择的文件列表
    private final List<File> selectedFiles;

    public FilePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        selectedFiles = new ArrayList<>();

        // 获取可用的哈希算法
        Vector<HashAlgorithm> availableAlgorithms = new Vector<>();
        for (HashAlgorithm algorithm : HashAlgorithm.values()) {
            if (ChecksumCalculator.isAlgorithmAvailable(algorithm)) {
                availableAlgorithms.add(algorithm);
            }
        }

        algorithmCombo = new JComboBox<>(availableAlgorithms);
        algorithmCombo.setPreferredSize(new Dimension(150, 25));

        addFilesButton = new JButton("添加文件");
        calculateButton = new JButton("计算校验值");
        compareButton = new JButton("对比校验值");
        clearButton = new JButton("清除");
        cancelButton = new JButton("取消计算");
        cancelButton.setEnabled(false);

        initializeUI();
    }

    private void initializeUI() {
        // 创建算法选择面板
        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        algorithmPanel.add(new JLabel("选择算法："));
        algorithmPanel.add(algorithmCombo);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addFilesButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(compareButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);

        // 将面板添加到主面板
        add(algorithmPanel);
        add(buttonPanel);
    }

    public JComboBox<HashAlgorithm> getAlgorithmCombo() {
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

    public JButton getCancelButton() {
        return cancelButton;
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }
}
