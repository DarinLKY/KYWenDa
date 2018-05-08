package com.KyLee.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @program: zhihu0.1
 * @description: Feed流
 * @author: KyLee
 * @create: 2018-05-08 19:04
 **/
public class Feed {
    private int id;
    private int userId;
    private Date createdDate;
    private int type;
    //data内容为json。
    private String data;

    private JSONObject dataJSON = null;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    //当前端调用feed.xxx时，模板自动调用feed.get(xxx);实现多态。
    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
