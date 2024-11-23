package com.example.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

//拦截器，用于更换页面语言
public class I18nInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        Locale locale = Locale.ENGLISH; // 默认语言为英语

        // 从 URL 参数获取语言
        String lang = inv.getController().getPara("lang");
        if (lang != null) {
            if ("zh".equals(lang)) {
                locale = Locale.CHINESE;
            } else if ("en".equals(lang)) {
                locale = Locale.ENGLISH;
            }
            inv.getController().setSessionAttr("locale", locale);
        } else {
            // 从 Session 中获取语言
            Locale sessionLocale = inv.getController().getSessionAttr("locale");
            if (sessionLocale != null) {
                locale = sessionLocale;
            }
        }

        // 加载对应的语言包并转换为 Map
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", locale);
        Map<String, String> i18nMap = new HashMap<>();
        for (String key : bundle.keySet()) {
            i18nMap.put(key, bundle.getString(key));
        }

        // 设置到模板中
        inv.getController().setAttr("i18n", i18nMap);

        inv.invoke();
    }
}

