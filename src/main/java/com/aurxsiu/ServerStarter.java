package com.aurxsiu;

import com.aurxsiu.extern.ImageFunction;

import java.io.IOException;

public class ServerStarter {
    public static void main(String[] args) throws IOException {
        ServerChar serverChar = new ServerChar();// 启动服务器

        new ImageFunction.Server(serverChar);
    }
}
