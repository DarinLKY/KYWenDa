package com.KyLee.service;

import com.KyLee.dao.LoginTokenDAO;
import com.KyLee.dao.UserDAO;
import com.KyLee.model.LoginToken;
import com.KyLee.model.User;
import com.KyLee.util.MD5Util;
//import org.apache.commons.lang.StringUtils;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * @program: zhihu0.1
 * @description: 用户业务处理
 * @author: KyLee
 * @create: 2018-04-26 19:44
 **/
@Service
public class UserService {

    private int STATUS_VAILD = 0;

    @Autowired(required = false)
    private UserDAO userDAO;

    @Autowired(required = false)
    private LoginTokenDAO loginTokenDAO;

    public int insertUser(User user){
        return userDAO.addUser(user);
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    /**
     * @description:加载登录token，
     *              登陆已有账号需要把过期时间更新，
     *              没有loginToken则新建
     * @param userId
     * @return 已有的或者新生成的logintoken
     */
    private LoginToken addLoginToken(int userId){

        //若用户已有token，分配已有的token.
        LoginToken loginToken=loginTokenDAO.selectByUserId(userId);
        if (loginToken!=null){
            //每次登陆把状态与超时时间重置。
            loginTokenDAO.updateStatus(loginToken.getTokenKey(),0);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*24*5);
            loginTokenDAO.updateExpired(loginToken.getTokenKey(),date);
            return loginToken;
        }

        //否则，添加用户token.
        else {
                loginToken = new LoginToken();
                loginToken.setTokenKey(UUID.randomUUID().toString().replaceAll("-", ""));
                loginToken.setUserId(userId);
                loginToken.setStatus(STATUS_VAILD);
                Date date = new Date();
                //超时时间设置为5天。
                date.setTime(date.getTime() + 1000*3600*24*5);
                loginToken.setExpired(date);
                loginTokenDAO.addLoginToken(loginToken);
                return loginToken;
        }
    }


    /**
     * @description 注册检测,map 作为Service与Controller的信息传递渠道。
     * @param username
     * @param password
     * @return 包含失败信息的Map
     */
    public Map<String,Object> register(String username,String password){
        Map<String,Object> context = new HashMap<String,Object>();
        if (StringUtils.isEmpty(username)||StringUtils.containsWhitespace(username)){
            context.put("msg","用户名不能包含空格");
            return context;
        }


        if (StringUtils.isEmpty(password)){
            context.put("msg","密码不能为空");
            return context;
        }
        User user = userDAO.selectByName(username);
        if (user!=null){
            context.put("msg","用户名已存在");
            return context;
        }

        User regUser = new User();
        regUser.setName(username);
        regUser.setSalt(UUID.randomUUID().toString().substring(0, 5));
        regUser.setPassword(MD5Util.MD5(password+regUser.getSalt()));


        userDAO.addUser(regUser);

        LoginToken loginToken=addLoginToken(regUser.getId());
        context.put("loginTokenKey",loginToken.getTokenKey());
        return context;

    }



    /**
     * @description 登录，map 作为Service与Controller的信息传递渠道。
     * @param username
     * @param password
     * @return 包含loginTokenKey（哈希表的key值为loginTokenKey） 与
     *          失败信息（哈希表的key值为msg）的map
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isEmpty(username)||StringUtils.containsWhitespace(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!(MD5Util.MD5(password+user.getSalt()).equals(user.getPassword()))) {
            map.put("msg", "密码不正确");
            return map;
        }

        LoginToken loginToken = addLoginToken(user.getId());
        map.put("loginTokenKey", loginToken.getTokenKey());
        return map;
    }

    /**
     * @description 登出，修改状态为1.
     * @param tokenKey
     */
    public void logout(String tokenKey) {
        loginTokenDAO.updateStatus(tokenKey,1);
    }

}
