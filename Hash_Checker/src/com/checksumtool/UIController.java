package com.checksumtool;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class UIController {

    // 文件面板
    private final FilePanel filePanel;
    // 结果面板
    private final ResultPanel resultPanel;
    // 校验值计算器
    private final ChecksumCalculator calculator;
    // 进度对话框
    private final ProgressDialog progressDialog;
    // 当前工作线程
    private SwingWorker<Void, ProgressUpdate> currentWorker;

    // 构造函数，初始化各个面板和计算器
    public UIController(FilePanel filePanel, ResultPanel resultPanel, ChecksumCalculator calculator) {
        this.filePanel = filePanel;
        this.resultPanel = resultPanel;
        this.calculator = calculator;
        this.progressDialog = new ProgressDialog((JFrame) null);

        // 为取消按钮添加监听器
        this.progressDialog.getCancelButton().addActionListener(e -> cancelCalculation());
    }

    // 取消计算
    private void cancelCalculation() {
        if (currentWorker != null && !currentWorker.isDone()) {
            calculator.cancelCalculation();
            currentWorker.cancel(true);
            progressDialog.setVisible(false);
            filePanel.getCalculateButton().setEnabled(true);
            JOptionPane.showMessageDialog(null, "计算已取消");
        }
    }

    // 处理计算
    public void handleCalculation() {
        // 如果没有选择文件，则提示用户选择文件
        if (filePanel.getSelectedFiles().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请先选择文件！");
            return;
        }

        // 清空结果面板和计算器的校验值
        resultPanel.getResultArea().setText("");
        calculator.clearChecksums();
        // 获取选择的算法
        HashAlgorithm algorithm = (HashAlgorithm) filePanel.getAlgorithmCombo().getSelectedItem();

        // 禁用计算按钮，重置进度对话框
        filePanel.getCalculateButton().setEnabled(false);
        progressDialog.reset();

        // 创建工作线程
        currentWorker = new SwingWorker<>() {
            private long totalBytes = 0;
            private long processedBytes = 0;

            @Override
            protected Void doInBackground() throws Exception {

                // 计算总字节数
                for (File file : filePanel.getSelectedFiles()) {
                    totalBytes += file.length();
                }

                // 计算每个文件的校验值
                for (File file : filePanel.getSelectedFiles()) {
                    if (isCancelled()) {
                        break;
                    }

                    try {

                        final String fileName = file.getName();
                        // 更新进度对话框
                        SwingUtilities.invokeLater(() -> progressDialog.updateProgress(0, fileName));

                        // 计算校验值
                        String checksum = calculator.calculateChecksum(
                                file,
                                algorithm,
                                (bytes) -> {
                                    processedBytes += bytes;
                                    int progress = (int) ((processedBytes * 100) / totalBytes);
                                    // 更新进度对话框
                                    SwingUtilities.invokeLater(() -> progressDialog.updateProgress(progress, fileName));
                                });

                        // 如果计算被取消，则退出循环
                        if (checksum.equals("Calculation cancelled")) {
                            break;
                        }

                        // 将校验值存入计算器
                        calculator.getFileChecksums().put(file.getPath(), checksum);
                        // 发布进度更新
                        publish(new ProgressUpdate(
                                (int) ((processedBytes * 100) / totalBytes),
                                fileName,
                                String.format("%s: %s", algorithm, checksum)));
                    } catch (Exception e) {
                        // 发布错误信息
                        publish(new ProgressUpdate(
                                (int) ((processedBytes * 100) / totalBytes),
                                file.getName(),
                                "Error: " + e.getMessage()));
                    }
                }
                return null;
            }

            @Override
            protected void process(List<ProgressUpdate> chunks) {
                // 更新结果面板
                for (ProgressUpdate update : chunks) {
                    resultPanel.getResultArea().append(
                            update.getFileName() + "\n" + update.getChecksumInfo() + "\n\n");
                }
            }

            @Override
            protected void done() {
                // 隐藏进度对话框，启用计算按钮
                progressDialog.setVisible(false);
                filePanel.getCalculateButton().setEnabled(true);
                // 如果计算未取消，则提示计算完成
                if (!isCancelled()) {
                    JOptionPane.showMessageDialog(null, "校验值计算完成！");
                }
            }
        };

        // 执行工作线程，显示进度对话框
        currentWorker.execute();
        progressDialog.setVisible(true);
    }

    // 处理比较
    public void handleComparison() {
        // 如果没有计算校验值，则提示用户计算校验值
        if (calculator.getFileChecksums().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请先计算校验值！");
            return;
        }

        // 获取输入的校验值
        Set<String> inputChecksums = new HashSet<>(Arrays.asList(
                resultPanel.getCompareArea().getText().toLowerCase().split("\n")));

        // 显示比较结果对话框
        ComparisonResultDialog resultDialog = new ComparisonResultDialog(
                calculator.getFileChecksums(), inputChecksums);
        resultDialog.setVisible(true);
    }

    // 清空
    public void handleClear() {
        // 清空选择的文件和计算器的校验值
        filePanel.getSelectedFiles().clear();
        calculator.clearChecksums();
        // 清空结果面板和比较面板
        resultPanel.getResultArea().setText("");
        resultPanel.getCompareArea().setText("");
    }

    // 选择文件
    public void selectFiles() {
        // 创建文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        // 如果用户选择了文件，则添加到选择的文件列表中
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            for (File file : fileChooser.getSelectedFiles()) {
                if (file.isDirectory()) {
                    addFilesFromDirectory(file);
                } else {
                    filePanel.getSelectedFiles().add(file);
                }
            }
            // 更新文件列表
            updateFileList();
        }
    }

    // 从目录中添加文件
    private void addFilesFromDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addFilesFromDirectory(file);
                } else {
                    filePanel.getSelectedFiles().add(file);
                }
            }
        }
    }

    // 更新文件列表
    private void updateFileList() {
        StringBuilder sb = new StringBuilder("已选择的文件：\n");
        for (File file : filePanel.getSelectedFiles()) {
            sb.append(file.getPath()).append("\n");
        }
        resultPanel.getResultArea().setText(sb.toString());
    }
}
