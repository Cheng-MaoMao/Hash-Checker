package com.checksumtool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProgressDialog extends JDialog {
    // 进度条
    private final JProgressBar progressBar;
    // 状态标签
    private final JLabel statusLabel;
    // 取消按钮
    private final JButton cancelButton;
    // 文件名标签
    private final JLabel fileNameLabel;

    // 构造函数，传入父窗口
    public ProgressDialog(Frame owner) {
        super(owner, "计算进度", true);

        // 创建进度条
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        // 创建状态标签
        statusLabel = new JLabel("准备开始...");
        // 创建文件名标签
        fileNameLabel = new JLabel("", SwingConstants.LEFT);
        // 创建取消按钮
        cancelButton = new JButton("取消计算");

        // 初始化UI
        initializeUI();
    }

    // 初始化UI
    private void initializeUI() {
        // 设置布局管理器
        setLayout(new BorderLayout(10, 10));

        // 创建进度面板
        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建文件面板
        JPanel filePanel = new JPanel(new BorderLayout());
        filePanel.add(new JLabel("当前文件: "), BorderLayout.WEST);
        filePanel.add(fileNameLabel, BorderLayout.CENTER);

        // 将文件面板添加到进度面板
        progressPanel.add(filePanel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(statusLabel, BorderLayout.SOUTH);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(cancelButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // 将进度面板和按钮面板添加到窗口
        add(progressPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 设置窗口大小和位置
        setSize(400, 150);
        setLocationRelativeTo(null);
        // 设置窗口关闭操作
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // 添加窗口监听器
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                cancelButton.doClick();
            }
        });
    }

    // 更新进度
    public void updateProgress(int progress, String fileName) {

        progress = Math.min(100, Math.max(0, progress));

        progressBar.setValue(progress);
        progressBar.setString(progress + "%");
        fileNameLabel.setText(fileName);
        statusLabel.setText(String.format("正在计算: %s (%d%%)", fileName, progress));
    }

    // 设置状态文本
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }

    // 获取取消按钮
    public JButton getCancelButton() {
        return cancelButton;
    }

    // 重置
    public void reset() {
        progressBar.setValue(0);
        progressBar.setString("0%");
        fileNameLabel.setText("");
        statusLabel.setText("准备开始...");
    }
}
