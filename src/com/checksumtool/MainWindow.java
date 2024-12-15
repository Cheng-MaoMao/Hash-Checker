package com.checksumtool;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    // 定义文件面板、结果面板、校验计算器和UI控制器
    private final FilePanel filePanel;
    private final ResultPanel resultPanel;
    private final ChecksumCalculator calculator;
    private final UIController uiController;

    // 构造函数，初始化窗口标题、关闭操作、布局、校验计算器、文件面板、结果面板和UI控制器
    public MainWindow() {
        setTitle("文件校验工具");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        calculator = new ChecksumCalculator();
        filePanel = new FilePanel();
        resultPanel = new ResultPanel();
        uiController = new UIController(filePanel, resultPanel, calculator);

        // 初始化UI
        initializeUI();
        // 设置监听器
        setupListeners();

        setSize(1000, 600);
        setLocationRelativeTo(null);
    }

    // 初始化UI，将文件面板添加到窗口的北边，结果面板添加到窗口的中间
    private void initializeUI() {
        add(filePanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
    }

    // 设置监听器，为文件面板的按钮添加事件监听器
    private void setupListeners() {
        filePanel.getCalculateButton().addActionListener(e -> uiController.handleCalculation());
        filePanel.getClearButton().addActionListener(e -> uiController.handleClear());
        filePanel.getCompareButton().addActionListener(e -> uiController.handleComparison());
        filePanel.getAddFilesButton().addActionListener(e -> uiController.selectFiles());
    }
}
