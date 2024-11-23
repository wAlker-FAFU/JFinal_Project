package com.example.controller;

import com.example.validator.UserValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.example.model.User;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController extends Controller {

    // 用户列表（分页功能）
    public void list() {
        int page = getParaToInt("page", 1); // 当前页码
        int pageSize = 6; // 每页6条记录

        // 从数据库查询分页数据
        Page<User> userPage = User.dao.paginate(page, pageSize, "SELECT *", "FROM user");

        // 转换分页数据为模板需要的格式
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("list", userPage.getList());
        pageData.put("totalPage", userPage.getTotalPage());
        pageData.put("pageNumber", userPage.getPageNumber());

        // 生成页码列表
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= userPage.getTotalPage(); i++) {
            pageNumbers.add(i);
        }

        // 设置模板变量
        setAttr("users", pageData.get("list"));
        setAttr("totalPage", pageData.get("totalPage"));
        setAttr("pageNumber", pageData.get("pageNumber"));
        setAttr("pageNumbers", pageNumbers);

        // 渲染用户列表页面
        render("/user/userList.html");
    }

    // 新增用户
    public void add() {
        // 查询当前最大 ID，生成新的 ID
        Integer maxId = Db.queryInt("SELECT MAX(id) FROM user");
        int newId = (maxId != null ? maxId : 0) + 1;

        // 创建一个新的用户对象并设置默认值
        User newUser = new User();
        newUser.set("id", newId); // 设置新的 ID

        // 将用户对象传递给模板
        setAttr("user", newUser);

        // 渲染用户编辑页面
        render("/user/userEdit.html");
    }

    // 编辑用户
    public void edit() {
        int id = getParaToInt("id");
        setAttr("user", User.dao.findById(id)); // 查找用户并传递给前端
        render("/user/userEdit.html");
    }

    // 保存用户信息（新增或更新）
    @Before(UserValidator.class)
    public void save() {
        User user = getModel(User.class, "user");

        // 判断是否为新增
        if (user.get("id") == null || User.dao.findById(user.get("id")) == null) {
            // 新增用户
            user.save();
        } else {
            // 更新用户
            user.update();
        }

        // 保存后跳转到用户列表页面
        redirect("/user/list");
    }

    // 删除用户及其关联订单
    @Before(Tx.class)
    public void delete() {
        int userId = getParaToInt("userId");

        // 删除用户关联的订单
        Db.update("DELETE FROM `order` WHERE user_id = ?", userId);

        // 删除用户
        Db.update("DELETE FROM user WHERE id = ?", userId);

        // 删除后跳转到用户列表页面
        redirect("/user/list");
    }

    // 查询用户关联的订单信息
    public void getOrders() {
        try {
            int userId = getParaToInt("userId");
            System.out.println("接收到的用户ID：" + userId);

            // 查询订单及对应产品名称
            List<Record> orders = Db.find(
                    "SELECT o.id, o.quantity, o.total_price, p.name AS product_name " +
                            "FROM `order` o " +
                            "LEFT JOIN `product` p ON o.product_id = p.id " +
                            "WHERE o.user_id = ?",
                    userId
            );

            if (orders == null || orders.isEmpty()) {
                System.out.println("该用户无关联订单！");
                renderJson("orders", new ArrayList<>());
            } else {
                System.out.println("查询到的订单：" + orders);
                renderJson("orders", orders);
            }
        } catch (Exception e) {
            renderJson("error", "查询失败，请检查数据库或SQL语句！");
        }
    }
}
