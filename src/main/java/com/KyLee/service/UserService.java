package com.KyLee.service;

import com.KyLee.dao.UserDAO;
import com.KyLee.model.User;
import com.KyLee.util.MD5Util;
//import org.apache.commons.lang.StringUtils;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: zhihu0.1
 * @description: 用户业务处理
 * @author: KyLee
 * @create: 2018-04-26 19:44
 **/
@Service
public class UserService {

    @Autowired(required = false)
    private UserDAO userDAO;

    public int insertUser(User user){
        return userDAO.addUser(user);
    }

    public ArrayList<User> getLatesetUsers(int offset, int limit){
        return userDAO.selectLatestUsers(offset,limit);
    }

    //register_msg 作为Service与Controller的信息传递渠道。
    public Map<String,String> register(String username,String password){
        Map<String,String> context = new HashMap<String,String>();
        if (StringUtils.isEmpty(username)||StringUtils.containsWhitespace(username)){
            context.put("register_msg","用户名不能包含空格");
            return context;
        }


        if (StringUtils.isEmpty(password)){
            context.put("register_msg","密码不能为空");
            return context;
        }
        User user = userDAO.selectByName(username);
        if (user!=null){
            context.put("register_msg","用户名已存在");
        }

        User regUser = new User();
        regUser.setName(username);
        regUser.setSlat(UUID.randomUUID().toString().substring(0, 5));
        regUser.setPassword(MD5Util.MD5(password+regUser.getSlat()));

        userDAO.addUser(regUser);
        return context;

    }

}
