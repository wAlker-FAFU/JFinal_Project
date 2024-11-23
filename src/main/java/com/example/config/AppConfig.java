package com.example.config;

import com.example.interceptor.I18nInterceptor;
import com.example.model.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.template.Engine;
import com.jfinal.plugin.druid.DruidPlugin;
import com.example.controller.*;

import java.lang.reflect.Type;

public class AppConfig extends JFinalConfig {
        public AppConfig() {
    }

    @Override
    public void configConstant(Constants constants) {
        constants.setDevMode(true); // 开发模式
    }
    //路由配置
    @Override
    public void configRoute(Routes routes) {
        // 配置路由
//        routes.add("/", IndexController.class);
//        routes.add("/user", UserController.class);
//        routes.add("/product", ProductController.class);
//        routes.add("/order", OrderController.class);
        //实现前后端路由分离
        //做到了路由拆分、模块化
        routes.add(new FrontRoutes()); // 前端路由
        routes.add(new ApiRoutes());   // 后端 API 路由

    }

    @Override
    public void configEngine(Engine engine) {
        engine.setDevMode(true); // 开发模式实时加载模板
        engine.setBaseTemplatePath("src/main/webapp");//设置模板引擎路径
    }
    //数据库连接
    @Override
    public void configPlugin(Plugins plugins) {
        // 配置数据源插件
        DruidPlugin dp = new DruidPlugin("jdbc:mysql://localhost:3306/jfinal_project",
                "root",
                "123456");
        plugins.add(dp);
        // 配置 ActiveRecord 插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        plugins.add(arp);

        // 映射表到模型
        arp.addMapping("user", "id", User.class);
        arp.addMapping("product", "id", Product.class);
        arp.addMapping("order", "id", Order.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {
        //全局拦截器，用于更新语言
        me.addGlobalActionInterceptor(new I18nInterceptor());
    }

    @Override
    public void configHandler(Handlers handlers) {
    }
}

