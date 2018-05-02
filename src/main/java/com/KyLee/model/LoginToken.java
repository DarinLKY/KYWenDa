package com.KyLee.model;

import java.util.Date;

/**
 * @program: zhihu0.1
 * @description: 用户使用的登录token，与Cookie对应。
 * @author: KyLee
 * @create: 2018-05-01 18:06
 **/
public class LoginToken {

    private int id ;
    private int userId;
    private Date expired;
    private int status;
    private String tokenKey;

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String key) {
        this.tokenKey = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
