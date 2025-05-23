package com.aurxsiu.extern;

import com.aurxsiu.ClientChar;

import javax.swing.*;
import java.awt.*;

public class ImageFunction {
    private final JPanel messagePanel;
    private final ClientChar client;

    public ImageFunction(ClientChar client){
        this.client = client;
        messagePanel = client.getMessagePanel();
    }
    private void addImageMessage(ImageIcon image, boolean fromMe) {
        JPanel panel = new JPanel(new FlowLayout(fromMe ? FlowLayout.RIGHT : FlowLayout.LEFT));
        Image img = image.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(img));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(label);
        messagePanel.add(panel);
        client.refreshChat();
    }
}
