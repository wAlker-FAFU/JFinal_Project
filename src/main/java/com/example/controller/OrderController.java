package com.example.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.example.model.Order;
import com.jfinal.plugin.activerecord.Record;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderController extends Controller {
// page,6每一页显示6条数据
public void list() {
    int page = getParaToInt("page", 1);
    int pageSize = 6;

    // 读取 SQL 文件内容
    String sqlFilePath = "src/main/resources/code.sql"; // 替换为你的 code.sql 文件路径
    String sqlQuery = readSqlFromFile(sqlFilePath);

    // 拆分 SQL 查询，适配 Db.paginate
    String[] sqlParts = sqlQuery.split("FROM", 2); // 分离 SELECT 和 FROM 部分
    if (sqlParts.length < 2) {
        throw new IllegalArgumentException("SQL 文件内容格式错误，请检查");
    }

    String select = sqlParts[0].trim();        // SELECT 部分
    String sqlExceptSelect = "FROM " + sqlParts[1].trim(); // FROM 部分

    // 调用分页方法
    Page<Record> orderPage = Db.paginate(page, pageSize, select, sqlExceptSelect);
    // 设置模板变量
    set("orders", orderPage.getList());
    set("pageNumber", orderPage.getPageNumber());
    set("totalPage", orderPage.getTotalPage());

    // 生成页码列表
    List<Integer> pageNumbers = new ArrayList<>();
    for (int i = 1; i <= orderPage.getTotalPage(); i++) {
        pageNumbers.add(i);
    }
    set("pageNumbers", pageNumbers);

    // 设置 MIME 类型（可选，确保是 HTML）
   // getResponse().setContentType("text/html;charset=UTF-8");

    // 渲染模板
    render("orderList.html");
}

    /**
     * 从文件中读取 SQL 内容
     */
//    private String readSqlFromFile(String filePath) {
//        StringBuilder sqlBuilder = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                sqlBuilder.append(line).append("\n");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("读取 SQL 文件失败：" + e.getMessage(), e);
//        }
//        return sqlBuilder.toString();
//    }
    private String readSqlFromFile(String filePath) {
        StringBuilder sqlBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append(" "); // 使用空格拼接，避免换行导致语法错误
            }
        } catch (IOException e) {
            throw new RuntimeException("读取 SQL 文件失败：" + e.getMessage(), e);
        }
        return sqlBuilder.toString().trim(); // 去掉末尾多余的空格
    }

    public void add() {
        Integer maxId = Db.queryInt("SELECT MAX(id) FROM `order`");
        int newId = (maxId != null ? maxId : 0) + 1;

        // 创建一个新的订单对象并设置默认值
        Order newOrder = new Order();
        newOrder.set("id", newId);

        // 将订单对象传递给模板
        setAttr("order", newOrder);

        // 渲染订单编辑页面
        render("/order/orderEdit.html");
    }

    public void edit() {
        int id = getParaToInt("id");
        Order order = Order.dao.findById(id);
        setAttr("order", order);
        render("/order/orderEdit.html");
    }

    public void save() {
        // 从表单中接收订单数据
        try {
            Order order = getModel(Order.class, "order");
            if (order.get("id") == null || Order.dao.findById(order.get("id")) == null) {
                order.save(); // 新增
            } else {
                order.update(); // 更新
            }
            redirect("/order/list");
        } catch (Exception e) {
            e.printStackTrace();
            renderText("订单保存失败：" + e.getMessage());
        }
    }

    public void delete() {
        int id = getParaToInt("id");
        Order.dao.deleteById(id);
        redirect("/order/list");
    }
}
