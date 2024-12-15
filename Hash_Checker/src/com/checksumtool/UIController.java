package com.checksumtool;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class UIController {
    // 定义文件面板、结果面板和校验值计算器
    private final FilePanel filePanel;
    private final ResultPanel resultPanel;
    private final ChecksumCalculator calculator;
    private JProgressBar progressBar;
    private JDialog progressDialog;

    // 构造函数，初始化文件面板、结果面板和校验值计算器
    public UIController(FilePanel filePanel, ResultPanel resultPanel, ChecksumCalculator calculator) {
        this.filePanel = filePanel;
        this.resultPanel = resultPanel;
        this.calculator = calculator;
        initializeProgressDialog();
    }

    // 初始化进度对话框
    private void initializeProgressDialog() {
        progressDialog = new JDialog((JFrame) null, "计算进度", true);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel();
        panel.add(new JLabel("正在计算校验值: "));
        panel.add(progressBar);

        progressDialog.add(panel);
        progressDialog.setSize(300, 100);
        progressDialog.setLocationRelativeTo(null);
        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    // 处理文件选择
    public void handleFileSelection() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            for (File file : fileChooser.getSelectedFiles()) {
                if (file.isDirectory()) {
                    addFilesFromDirectory(file);
                } else {
                    filePanel.getSelectedFiles().add(file);
                }
            }
            updateFileList();
        }
    }

    // 递归添加目录中的文件
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

    // 处理校验值计算
    public void handleCalculation() {
        if (filePanel.getSelectedFiles().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请先选择文件！");
            return;
        }

        resultPanel.getResultArea().setText("");
        calculator.clearChecksums();
        String algorithm = (String) filePanel.getAlgorithmCombo().getSelectedItem();

        SwingWorker<Void, ProgressUpdate> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                int totalFiles = filePanel.getSelectedFiles().size();
                int currentFile = 0;

                for (File file : filePanel.getSelectedFiles()) {
                    currentFile++;
                    String checksum = calculator.calculateChecksum(file, algorithm);
                    calculator.getFileChecksums().put(file.getPath(), checksum);

                    int progress = (currentFile * 100) / totalFiles;
                    publish(new ProgressUpdate(
                            progress,
                            file.getName(),
                            String.format("%s: %s", algorithm, checksum)));
                }
                return null;
            }

            @Override
            protected void process(List<ProgressUpdate> chunks) {
                for (ProgressUpdate update : chunks) {
                    progressBar.setValue(update.getProgress());
                    progressBar.setString(String.format("%d%% - %s",
                            update.getProgress(), update.getFileName()));
                    resultPanel.getResultArea().append(
                            update.getFileName() + "\n" + update.getChecksumInfo() + "\n\n");
                }
            }

            @Override
            protected void done() {
                progressDialog.setVisible(false);
                JOptionPane.showMessageDialog(null, "校验值计算完成！");
            }
        };

        worker.execute();
        progressBar.setValue(0);
        progressDialog.setVisible(true);
    }

    // 处理校验值比较
    public void handleComparison() {
        if (calculator.getFileChecksums().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请先计算校验值！");
            return;
        }

        Set<String> inputChecksums = new HashSet<>(Arrays.asList(
                resultPanel.getCompareArea().getText().toLowerCase().split("\n")));

        ComparisonResultDialog resultDialog = new ComparisonResultDialog(
                calculator.getFileChecksums(), inputChecksums);
        resultDialog.setVisible(true);
    }

    // 处理清空操作
    public void handleClear() {
        filePanel.getSelectedFiles().clear();
        calculator.clearChecksums();
        resultPanel.getResultArea().setText("");
        resultPanel.getCompareArea().setText("");
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
