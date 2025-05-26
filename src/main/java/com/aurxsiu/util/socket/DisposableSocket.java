package com.aurxsiu.util.socket;

import java.io.*;
import java.net.Socket;

/**
 * 返回socket方便后续发送,接收数据等处理
 * 不会关闭流,只会直接关闭socket(只在just方法)
 * 传socket为参数而不是ip和port,主要注重一个流的处理
 * */
public class DisposableSocket {
    //todo 升级nio
    public static class Send{
        public static void sendFile(Socket socket, File file) throws IOException {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                OutputStream outputStream = socket.getOutputStream();
                byte[] bytes = new byte[1024];
                while(true){
                    int read = fileInputStream.read(bytes);
                    if(read<0){
                        break;
                    }
                    outputStream.write(bytes,0,read);
                }
            }
        }

        public static Socket sendInfo(Socket socket,String info) throws IOException{
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(info.getBytes());
            return socket;
        }
    }
    public static class Accept{
        /**
         * 不做文件处理,因为创建文件的结果不确定,不好处理
         * */
        public static void acceptFile(FileOutputStream outputStream,Socket socket) throws IOException {
            try (InputStream inputStream = socket.getInputStream()) {
                byte[] bytes = new byte[1024];
                while(true){
                    int read = inputStream.read(bytes);
                    if(read<0){
                        break;
                    }
                    outputStream.write(bytes,0,read);
                }
            }
        }

    }

}
