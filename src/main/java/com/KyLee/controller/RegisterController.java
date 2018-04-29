package com.KyLee.controller;

import com.KyLee.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    UserService userService;


    @RequestMapping(path={"/register/"},method = {RequestMethod.POST})
    public String register (Model model,
                           HttpServletResponse response,
                           HttpServletRequest request,
                            @RequestParam("username") String username,
                            @RequestParam("password") String password){
         try{
             Map<String,String> map = userService.register(username,password);
            if (map.get("register_msg")!=null){
                model.addAttribute("msg",map.get("register_msg"));
                return "login";
            }
            return "/";
         }catch (Exception e){
             logger.error("注册异常： "+e.getMessage());
             return "login";
         }
    }
    @RequestMapping("/")
    public String test (Model model){
        return "login";
    }
}
