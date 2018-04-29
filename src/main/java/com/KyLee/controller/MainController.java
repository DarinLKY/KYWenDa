package com.KyLee.controller;

import com.KyLee.model.User;
import com.KyLee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: zhihu0.1
 * @description: 测试网页。
 * @author: KyLee
 * @create: 2018-04-26 14:21
 **/

//@Controller
public class MainController {


    @Autowired
    UserService userService;


    @RequestMapping(path={"/"})
    @ResponseBody
    public String request (Model model,
                           HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession session
                           //@PathVariable("userid") int userid ,
                           //@RequestParam("key")String key,
                           //@RequestParam("re") String re
                           ){
        return "hello,spring!";
    }

    @RequestMapping(path={"/sqltest"})
    @ResponseBody
    public String sqltest (Model model,
                           HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession session){
                           //@PathVariable("userid") int userid){
        User user = new User();
        user.setEmail("fdf@qq.com");
        user.setId(3);
        user.setName("test");
        user.setPassword("3");
        userService.insertUser(user);
        return "成功添加用户";
    }


}
