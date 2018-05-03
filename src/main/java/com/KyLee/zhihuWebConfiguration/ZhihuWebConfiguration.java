package com.KyLee.zhihuWebConfiguration;

import com.KyLee.Interceptor.PageTransInterceptor;
import com.KyLee.Interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @program: zhihu0.1
 * @description: 用于各类拦截器，bean，Mapper的配置
 * @author: KyLee
 * @create: 2018-05-02 20:08
 **/
@Component
public class ZhihuWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    TokenInterceptor tokenInterceptor;

    @Autowired
    PageTransInterceptor pageTransInterceptor;

    //限制进入的路径，如果限制成功则在pageTransInterceptor中设置限制页面。
    private String restrictPath="/user/*";
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor);
        registry.addInterceptor(pageTransInterceptor).addPathPatterns(restrictPath);
        super.addInterceptors(registry);
    }
}
