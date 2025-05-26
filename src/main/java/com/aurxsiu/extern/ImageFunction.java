package com.aurxsiu.extern;

import com.aurxsiu.ClientChar;
import com.aurxsiu.ServerChar;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageFunction {
    public final static int PORT = 25424;

    public static class Client {
        private final JPanel messagePanel;
        private final ClientChar client;
        private final Socket socket;
        public final File cacheDir = new File(System.getProperty("user.dir")+"/cache");
        public final File imageDir = new File(cacheDir.getAbsolutePath()+"/image");
        public Client(ClientChar client) throws IOException {
            this.client = client;
            messagePanel = client.getMessagePanel();
            JPanel inputPanel = client.getInputPanel();

            if (!imageDir.isDirectory()) {
                imageDir.mkdirs();
            }

            socket = new Socket("127.0.0.1",PORT);
            new DataOutputStream(socket.getOutputStream()).writeUTF(client.getName());
            JButton imageButton = new ImageSendButton("发送图片",socket);
            new Thread(()->{
                try {
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    AtomicInteger atomicInteger = new AtomicInteger();
                    while(true){
                        String userName = inputStream.readUTF();
                        long file_length = inputStream.readLong();
                        File file = new File(imageDir.getAbsolutePath()+"/"+atomicInteger.addAndGet(1)+"-"+userName+".png");

                        if (!file.createNewFile()) {
                            System.err.println("file already exists");
                        }
                        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                            fileOutputStream.write(inputStream.readNBytes(Math.toIntExact(file_length)));
                        }
                        addImageMessage(new ImageIcon(file.getAbsolutePath()));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }).start();
            inputPanel.add(imageButton);
        }

        private void addImageMessage(ImageIcon image) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            Image img = image.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(img));
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panel.add(label);
            messagePanel.add(panel);
            client.refreshChat();
        }

        //todo
        public void close() throws IOException {
            socket.close();
        }
    }

    public static class Server {
        private final HashMap<String,Socket> connections = new HashMap<>();
        private final ServerSocket serverSocket;
        private final JTextArea logArea;
        AtomicInteger imageIndex = new AtomicInteger();

        public final File cacheDir = new File(System.getProperty("user.dir")+"/data");
        public final File imageDir = new File(cacheDir.getAbsolutePath()+"/image");

        public Server(ServerChar serverChar) throws IOException {
            logArea = serverChar.getJta();
            if (!imageDir.isDirectory()){
                imageDir.mkdirs();
            }

            serverSocket = new ServerSocket(PORT);

            new Thread(() -> {
                try{
                    while (true) {
                        Socket accept = serverSocket.accept();
                        DataInputStream input = new DataInputStream(accept.getInputStream());
                        String userName = input.readUTF();
                        connections.put(userName,accept);
                        //todo 看着线程安全,急了
                        new Thread(() -> {
                            try {
                                while (true) {
                                    int file_length = (int) input.readLong();
                                    File file = new File(System.getProperty("user.dir") + "/data/image/" + imageIndex.addAndGet(1) + "-" + userName + ".png");
                                    if (!file.createNewFile()) {
                                        System.err.println("file already exist");
                                    }
                                    byte[] get = input.readNBytes(file_length);
                                    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                                        fileOutputStream.write(get);
                                    }
                                    boardCast(get,userName);
                                    logArea.append("["+userName+"]: send image("+file.getName()+")");
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).start();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();


        }
        private synchronized void boardCast(byte[] data,String userName) throws IOException {
            for (String s : connections.keySet()) {
                Socket socket = connections.get(s);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                outputStream.writeUTF(userName);
                outputStream.writeLong(data.length);
                outputStream.write(data);
            }
        }


        public void close() throws IOException {
            serverSocket.close();
        }
    }
}
