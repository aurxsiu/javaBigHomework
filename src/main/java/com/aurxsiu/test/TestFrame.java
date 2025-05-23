package com.aurxsiu.test;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;

public class TestFrame extends JFrame {
    public TestFrame() {
        this.setVisible(true);
        this.setBounds(700,300,300,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭操作


        JTextPane pane = new JTextPane(new HTMLDocument());
        this.add(pane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new TestFrame();
    }
}
