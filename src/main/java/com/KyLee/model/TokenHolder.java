package com.KyLee.model;

import org.springframework.stereotype.Component;

/**
 * @program: zhihu0.1
 * @description: 服务器的每个线程中的User持有工具
 * @author: KyLee
 * @create: 2018-05-02 16:04
 **/
@Component
public class TokenHolder {
    private static ThreadLocal<User> tokenholder = new ThreadLocal<User>();

    public User getUser(){
        return tokenholder.get();
    }
    public void  setUser(User user){
        tokenholder.set(user);
    }
    public void remove (){
        tokenholder.remove();
    }
}
