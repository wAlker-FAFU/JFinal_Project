package com.example.controller;

import com.jfinal.core.Controller;

public class IndexController extends Controller {
    public void index() {
        render("/index.html"); // 渲染默认首页
    }
}
