package com.checksumtool;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public class ComparisonResultDialog extends JDialog {
    // 声明一个JTextPane对象，用于显示对比结果
    private final JTextPane resultPane;

    // 构造函数，接收文件校验值和输入校验值的集合
    public ComparisonResultDialog(Map<String, String> fileChecksums, Set<String> inputChecksums) {
        super((Frame) null, "对比结果", true);

        // 初始化JTextPane对象
        resultPane = new JTextPane();
        resultPane.setContentType("text/html");
        resultPane.setEditable(false);

        // 初始化UI界面
        initializeUI();
        // 显示对比结果
        displayResults(fileChecksums, inputChecksums);
    }

    // 初始化UI界面
    private void initializeUI() {
        setLayout(new BorderLayout());
        // 创建一个滚动面板，用于显示JTextPane对象
        JScrollPane scrollPane = new JScrollPane(resultPane);
        add(scrollPane, BorderLayout.CENTER);

        // 创建一个关闭按钮，点击后关闭对话框
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dispose());

        // 创建一个按钮面板，用于放置关闭按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 设置对话框大小和位置
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    // 显示对比结果
    private void displayResults(Map<String, String> fileChecksums, Set<String> inputChecksums) {
        // 创建一个StringBuilder对象，用于存储HTML格式的对比结果
        StringBuilder htmlResult = new StringBuilder(
                "<html><body style='font-family: Arial; font-size: 12px; margin: 10px;'>");

        // 添加标题
        htmlResult.append("<h2>校验结果</h2>");

        // 遍历文件校验值集合
        for (Map.Entry<String, String> entry : fileChecksums.entrySet()) {
            String filePath = entry.getKey();
            String calculatedChecksum = entry.getValue().toLowerCase();
            // 判断输入校验值集合中是否包含计算的校验值
            boolean matched = inputChecksums.contains(calculatedChecksum);
            // 根据匹配结果设置颜色
            String color = matched ? "#4CAF50" : "#F44336";

            // 添加文件路径、计算的校验值、对比的校验值和状态
            htmlResult.append("<div style='border: 1px solid #ddd; padding: 10px; margin-bottom: 10px;'>");
            htmlResult.append("<p><b>文件路径:</b> ").append(filePath).append("</p>");
            htmlResult.append("<p><b>计算的校验值:</b> <span style='color: ").append(color)
                    .append("'>").append(calculatedChecksum).append("</span></p>");

            // 获取匹配的输入校验值
            String matchedInput = inputChecksums.stream()
                    .filter(input -> input.equalsIgnoreCase(calculatedChecksum))
                    .findFirst()
                    .orElse("未找到匹配的校验值");

            htmlResult.append("<p><b>对比的校验值:</b> <span style='color: ").append(color)
                    .append("'>").append(matchedInput).append("</span></p>");

            htmlResult.append("<p><b>状态:</b> <span style='color: ").append(color)
                    .append("'>").append(matched ? "匹配" : "不匹配").append("</span></p>");
            htmlResult.append("</div>");
        }

        // 添加HTML结尾
        htmlResult.append("</body></html>");
        // 设置JTextPane对象的内容
        resultPane.setText(htmlResult.toString());
    }
}
