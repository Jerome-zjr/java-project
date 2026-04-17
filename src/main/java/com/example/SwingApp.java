package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Swing GUI 版本：演示如何让 JTextField 正确响应 Enter 键
 * 常见问题：没有为输入框添加 ActionListener，或只给按钮添加了监听器
 * 解决方案：同时为 JTextField 和 JButton 添加 ActionListener
 */
public class SwingApp extends JFrame {

    private final JTextField inputField;
    private final JTextArea outputArea;
    private final JButton sendButton;

    public SwingApp() {
        setTitle("Enter 键响应示例");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // 输出区域
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setText("欢迎！在下方输入内容，按 Enter 或点击「发送」按钮。\n\n");

        JScrollPane scrollPane = new JScrollPane(outputArea);

        // 输入区域
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputField.setToolTipText("在此输入，按 Enter 键确认");

        sendButton = new JButton("发送");
        sendButton.setMnemonic(KeyEvent.VK_ENTER);

        // 关键：为 JTextField 添加 ActionListener，使其响应 Enter 键
        ActionListener onSubmit = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput();
            }
        };
        inputField.addActionListener(onSubmit);   // 输入框按 Enter 触发
        sendButton.addActionListener(onSubmit);    // 按钮点击触发

        // 底部面板
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        // 布局
        setLayout(new BorderLayout(0, 0));
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 让输入框默认获取焦点
        SwingUtilities.invokeLater(() -> inputField.requestFocusInWindow());
    }

    private void handleInput() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) {
            outputArea.append("[提示] 请输入内容后再按 Enter。\n");
        } else {
            outputArea.append(">>> " + text + "\n");

            // 尝试解析为数字并计算平方
            try {
                double number = Double.parseDouble(text);
                outputArea.append(String.format("    结果：%.2f 的平方 = %.2f%n", number, number * number));
            } catch (NumberFormatException e) {
                outputArea.append(String.format("    收到文本：「%s」（%d 个字符）%n", text, text.length()));
            }
            outputArea.append("\n");
        }

        inputField.setText("");
        // 滚动到底部
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        // 在 EDT 线程中启动 Swing 应用（正确做法）
        SwingUtilities.invokeLater(() -> {
            SwingApp app = new SwingApp();
            app.setVisible(true);
        });
    }
}
