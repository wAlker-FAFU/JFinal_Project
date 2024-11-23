package com.example.config;

import com.example.controller.IndexController;
import com.example.controller.OrderController;
import com.example.controller.ProductController;
import com.example.controller.UserController;
import com.jfinal.config.Routes;

public class FrontRoutes extends Routes {
    @Override
    public void config() {
        // 首页路由
        add("/", IndexController.class, "/");

        // 订单相关页面
        add("/order", OrderController.class, "/order");

        // 产品相关页面
        add("/product", ProductController.class, "/product");

        // 用户相关页面
        add("/user", UserController.class, "/user");
    }
}
