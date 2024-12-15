package com.checksumtool;

// 定义App类
public class App {
    // 主方法
    public static void main(String[] args) {
        // 在事件分派线程中运行代码
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // 设置外观和感觉为系统默认
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // 打印异常信息
                e.printStackTrace();
            }
            // 创建MainWindow对象
            MainWindow mainWindow = new MainWindow();
            // 设置MainWindow可见
            mainWindow.setVisible(true);
        });
    }
}
