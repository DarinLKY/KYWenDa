package com.KyLee.Interceptor;

import com.KyLee.model.TokenHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.*;
/**
 * @program: zhihu0.1
 * @description: 页面跳转的功能拦截器,拦截游客进入用户页面。
 *               并且拦截登录的用户访问其他用户的界面。
 * @author: KyLee
 * @create: 2018-05-02 17:37
 **/
@Component
public class PageTransInterceptor implements HandlerInterceptor {

    @Autowired
    TokenHolder tokenHolder;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (tokenHolder.getUser() == null) {
            //这里跳转到/reglogin，后面的next=...不影响页面，只是加了变量。
            //httpServletRequest.getRequestURI()是原本的请求URL。
            httpServletResponse.sendRedirect("/reglogin?next=" + httpServletRequest.getRequestURI());
            return false;
        }
        if (tokenHolder.getUser() != null) {

            // /user/user_id截取用户id并检测。
            String pattern = "(.*)/user/(\\d+)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(httpServletRequest.getRequestURI());
            String user_id=null;
            if(m.find()) {
                user_id =httpServletRequest.getRequestURI().substring(m.start()+6,m.end());
            }

            //String user_id=httpServletRequest.getRequestURI().substring(6);
            int userId = Integer.parseInt(user_id);
            if(tokenHolder.getUser().getId()!=userId) {
                httpServletResponse.sendRedirect("/reglogin");
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
