package com.example.config;

import com.example.controller.OrderController;
import com.example.controller.ProductController;
import com.example.controller.UserController;
import com.jfinal.config.Routes;

public class ApiRoutes extends Routes {
    @Override
    public void config() {
        // 订单 API 路由
        add("/api/order", OrderController.class);

        // 产品 API 路由
        add("/api/product", ProductController.class);

        // 用户 API 路由
        add("/api/user", UserController.class);
    }
}
