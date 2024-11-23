package com.example;

import com.example.config.AppConfig;
import com.jfinal.server.undertow.UndertowServer;
public class Main {
    public static void main(String[] args) {
        UndertowServer server = UndertowServer.create(AppConfig.class);

        // 设置端口号（例如 9090）
        server.setPort(8080);

        // 设置上下文路径（例如 "/"）
        server.setContextPath("/");
        // 启动服务器
        server.start();
    }
}