package com.KyLee.service;

import com.KyLee.dao.CommentDAO;
import com.KyLee.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: zhihu0.1
 * @description: 评论服务
 * @author: KyLee
 * @create: 2018-05-04 11:20
 **/
@Service
public class CommentService {

    @Autowired(required = false)
    CommentDAO commentDAO;

    public List<Comment> getComment(int id,int type){
        return commentDAO.selectByEntity(id,type);
    }
    public int getCommentCount(int id,int type){
        return commentDAO.selectCountByEntity(id,type);
    }

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    boolean  deleteComment(int id ,int status){
        return commentDAO.updateStatus(id,status) > 0;
    }
}
