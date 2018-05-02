package com.KyLee.controller;

import com.KyLee.dao.LoginTokenDAO;
import com.KyLee.service.UserService;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @program: zhihu0.1
 * @description: 注册服务
 * @author: KyLee
 * @create: 2018-04-29 16:30
 **/
@Controller
public class RegAndLoginController {
    private static final Logger logger = LoggerFactory.getLogger(RegAndLoginController.class);
    @Autowired
    UserService userService;

    @Autowired(required = false)
    LoginTokenDAO loginTokenDAO;
    /**
     * @description:主页
     * @param model
     * @return 注册页面
     */

    /*
    @RequestMapping(path={"/login","/relogin"})
    public String test (Model model){
        return "login";
    }
*/

    /**
     * @description:登录注册公用入口
     * @param model
     * @param next
     * @return
     */
    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String regloginPage(Model model, @RequestParam(value = "next", required = false) String next) {
        model.addAttribute("next", next);
        return "login";
    }

    /**
     * @description:注册
     * @param model
     * @param response
     * @param request
     * @param username
     * @param password
     * @return 注册页面
     */
    @RequestMapping(path={"/register"},method = {RequestMethod.POST})
    public String register (Model model,
                           HttpServletResponse response,
                           HttpServletRequest request,
                            @RequestParam("username") String username,
                            @RequestParam(value = "next",required = false) String next,
                            @RequestParam("password") String password,
                            @RequestParam(value="rememberme", defaultValue = "false")boolean rememberme) {
        try {
            Map<String, Object> map = userService.register(username, password);

            if (map.containsKey("loginTokenKey")) {

                //加入Cookie
                Cookie cookie = new Cookie("loginToken", map.get("loginTokenKey").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);

                if (!(StringUtils.isEmpty(next))) {
                    //return 时依然要经历拦截器，所有直接写入Cookie就可以了，拦截器会再次读取Cookie
                    return "redirect:" + next;
                }
            } else if (map.get("msg") != null) {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
            return "redirect:/";
        } catch (Exception e) {
            logger.error("注册异常： " + e.getMessage());
            model.addAttribute("msg", "后台异常");
            return "login";
        }

    }

    /**
     * @description:登录
     * @param model
     * @param response
     * @param request
     * @param username
     * @param password
     * @param next
     * @param rememberme
     * @return
     */
    @RequestMapping(path={"/login"},method = {RequestMethod.POST})
    public String login (Model model,
                            HttpServletResponse response,
                            HttpServletRequest request,
                            @RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam(value = "next",required = false) String next,
                            @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme){
        try{
            Map<String,Object> map = userService.login(username,password);

            if (map.containsKey("loginTokenKey")){

                //加入Cookie
                Cookie cookie =new Cookie("loginToken", map.get("loginTokenKey").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                if (!(StringUtils.isEmpty(next))) {
                    //return 时依然要经历拦截器，所有直接写入Cookie就可以了，拦截器会再次读取Cookie
                    return "redirect:" + next;
                }
            }
            else if (map.get("msg")!=null){
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
            return "redirect:/";
        }catch (Exception e){
            logger.error("登录异常： "+e.getMessage());
            model.addAttribute("msg","后台异常");
            return "login";
        }
    }

    /**@description:登出
     * @des
     * @param tokenKey
     * @return
     */
    @RequestMapping(path={"/logout"},method = {RequestMethod.POST,RequestMethod.GET})
    public String logout (@CookieValue("loginToken") String tokenKey){
            userService.logout(tokenKey);
            return "redirect:/";
    }

}
