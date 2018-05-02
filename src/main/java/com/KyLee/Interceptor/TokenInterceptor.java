package com.KyLee.Interceptor;

import com.KyLee.dao.LoginTokenDAO;
import com.KyLee.dao.UserDAO;
import com.KyLee.model.LoginToken;
import com.KyLee.model.TokenHolder;
import com.KyLee.model.User;
import com.KyLee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @program: zhihu0.1
 * @description: 用于检测token的拦截器
 * @author: KyLee
 * @create: 2018-05-02 12:36
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    TokenHolder tokenHolder;
    @Autowired(required = false)
    UserDAO userDAO;

    @Autowired(required = false)
    LoginTokenDAO loginTokenDAO;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Cookie[] cookies = httpServletRequest.getCookies();
        String loginTokenKey =null;
        if(cookies!=null){
            for (Cookie cookie :cookies){
                if (cookie.getName().equals("loginToken")){
                    loginTokenKey = cookie.getValue();
                    break;
                }
            }
        }
        if (loginTokenKey!=null){
            LoginToken loginToken=loginTokenDAO.selectByKey(loginTokenKey);
            if(loginToken==null||loginToken.getStatus()!=0||(loginToken.getExpired().before(new Date()))){
                return true;
            }
            else{
                User user = userDAO.selectById(loginToken.getUserId());
                tokenHolder.setUser(user);
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        User user =tokenHolder.getUser();
        if(modelAndView!=null && user!=null){
            modelAndView.addObject("user",tokenHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
            tokenHolder.remove();
    }
}
