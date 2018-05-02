package com.KyLee.model;

/**
 * @program: zhihu0.1
 * @description: 用户模型Bean
 * @author: KyLee
 * @create: 2018-04-26 15:19
 **/
public class User {
    private int id ;
    private String name;

    private String password;
    private String salt;
    private String email;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setPassword(String password) {
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }


}
