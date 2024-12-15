package com.checksumtool;

import javax.swing.*;
import java.awt.*;

public class ResultPanel extends JPanel {
    // 定义两个文本区域和一个分割面板
    private final JTextArea resultArea;
    private final JTextArea compareArea;
    private final JSplitPane splitPane;

    // 构造函数
    public ResultPanel() {
        setLayout(new BorderLayout());

        resultArea = new JTextArea();
        compareArea = new JTextArea();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // 初始化UI
        initializeUI();
    }

    // 初始化UI
    private void initializeUI() {
        resultArea.setEditable(false);
        JScrollPane leftScrollPane = new JScrollPane(resultArea);
        JScrollPane rightScrollPane = new JScrollPane(compareArea);

        // 设置分割面板的左右组件
        splitPane.setLeftComponent(leftScrollPane);
        splitPane.setRightComponent(rightScrollPane);
        splitPane.setResizeWeight(0.5);

        // 创建标签面板
        JPanel labelPanel = new JPanel(new GridLayout(1, 2));
        labelPanel.add(new JLabel("计算结果："));
        labelPanel.add(new JLabel("输入要对比的校验值（每行一个）："));

        // 将标签面板和分割面板添加到主面板中
        add(labelPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    // 获取计算结果文本区域
    public JTextArea getResultArea() {
        return resultArea;
    }

    // 获取对比文本区域
    public JTextArea getCompareArea() {
        return compareArea;
    }
}
