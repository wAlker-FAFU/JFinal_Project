package com.example.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.example.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductController extends Controller {

    public void list() {
        int page = getParaToInt("page", 1); // 当前页码，默认为1
        int pageSize = 6; // 每页10条记录
        Page<Product> productPage = Product.dao.paginate(page, pageSize, "SELECT *", "FROM product");
        setAttr("products", productPage.getList());
        setAttr("pageNumber", productPage.getPageNumber());
        setAttr("totalPage", productPage.getTotalPage());
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= productPage.getTotalPage(); i++) {
            pageNumbers.add(i);
        }
        setAttr("pageNumbers", pageNumbers);
        render("/product/productList.html");
    }

    public void add() {
        Integer maxId = Db.queryInt("SELECT MAX(id) FROM product");
        int newId = (maxId != null ? maxId : 0) + 1;

        // 创建一个新的产品对象并设置默认值
        Product newProduct = new Product();
        newProduct.set("id", newId);

        // 将产品对象传递给模板
        setAttr("product", newProduct);

        // 渲染产品编辑页面
        render("/product/productEdit.html");
    }

    public void save() {
        // 从表单中接收产品数据
        Product product = getModel(Product.class, "product");

        // 判断是新增还是更新
        if (product.get("id") == null || Product.dao.findById(product.get("id")) == null) {
            product.save(); // 新增
        } else {
            product.update(); // 更新
    }
        // 保存后跳转到产品列表页面
        redirect("/product/list");
        }
    public void edit() {
        int id = getParaToInt("id");
        Product product = Product.dao.findById(id);
        setAttr("product", product);
        render("/product/productEdit.html");
    }
    public void delete() {
        int id = getParaToInt("id");
        Product.dao.deleteById(id);
        redirect("/product/list");
    }
}
