package com.aurxsiu.extern;

import com.aurxsiu.util.socket.DisposableSocket;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ImageSendButton extends JButton {
    public ImageSendButton(String text, Socket socket) {
        super(text);

        this.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
            chooser.setFileFilter(filter);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeLong(file.length());
                    DisposableSocket.Send.sendFile(socket, file);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}
