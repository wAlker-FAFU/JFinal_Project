package com.example.validator;
import com.example.model.User;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

//验证器，用于验证用户名是否按照要求来写
public class UserValidator extends Validator {
    @Override
    protected void validate(Controller c) {
        // 验证用户名：以字母开头，长度 4-12 位
        validateRegex("user.username",
                "^[a-zA-Z][a-zA-Z0-9_]{3,11}$",
                "usernameMsg",
                "用户名必须以字母开头，长度为4-12位");
    }
    @Override
    protected void handleError(Controller c) {
        if (c.getAttr("user") == null) {
        c.setAttr("user", new User()); // 确保 user 对象始终存在
    }
        // 设置错误信息到页面
        c.render("/user/userEdit.html");
    }
}
