package com.KyLee.model;

import java.util.Date;

/**
 * @program: zhihu0.1
 * @description: 评论
 * @author: KyLee
 * @create: 2018-05-04 11:09
 **/
public class Comment {
    /**
     * `id` INT NOT NULL AUTO_INCREMENT,
     `content` TEXT NOT NULL,
     `user_id` INT NOT NULL,
     `entity_id` INT NOT NULL,
     `entity_type` INT NOT NULL,
     `created_date` DATETIME NOT NULL,
     `status` INT NOT NULL DEFAULT 0,
     */
    private int id ;
    private String content;
    private int entityId;



    private int userId;
    //具体的键类型，1为问题，2为评论。
    private int entityType;
    private Date createdDate;
    //0为正常，1为屏蔽。
    private int status;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
