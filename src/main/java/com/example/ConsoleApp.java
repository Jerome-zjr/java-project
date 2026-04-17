package com.example;

import java.util.Scanner;

/**
 * 控制台版本：演示如何正确响应 Enter 键输入
 * 常见问题：混用 nextInt()/nextLine() 导致 Enter 未响应
 * 解决方案：统一使用 nextLine()，需要数字时再手动转换
 */
public class ConsoleApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== 控制台输入示例（按 Enter 确认）===");
        System.out.println("输入 'quit' 退出\n");

        while (true) {
            System.out.print("请输入内容：");
            // 统一用 nextLine() 读取整行，按 Enter 后立即响应
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("已退出。");
                break;
            }

            if (input.isEmpty()) {
                System.out.println("（未输入任何内容，请重试）");
                continue;
            }

            // 尝试解析为数字
            try {
                double number = Double.parseDouble(input);
                System.out.printf("您输入了数字：%.2f，其平方为：%.2f%n%n", number, number * number);
            } catch (NumberFormatException e) {
                System.out.printf("您输入了文本：「%s」（共 %d 个字符）%n%n", input, input.length());
            }
        }

        scanner.close();
    }
}
