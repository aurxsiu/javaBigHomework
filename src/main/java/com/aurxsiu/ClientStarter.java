package com.aurxsiu;

import com.aurxsiu.extern.ImageFunction;

import java.io.IOException;

public class ClientStarter {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        ClientChar clientChar = new ClientChar();// 创建客户端对象并初始化
        clientChar.init();

        new ImageFunction.Client(clientChar);
    }
}