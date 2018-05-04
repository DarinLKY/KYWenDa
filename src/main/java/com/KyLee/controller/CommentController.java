package com.KyLee.controller;

import com.KyLee.model.Comment;
import com.KyLee.model.Question;
import com.KyLee.model.TokenHolder;
import com.KyLee.service.CommentService;
import com.KyLee.service.QuestionService;
import jdk.nashorn.internal.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @program: zhihu0.1
 * @description: 评论C端
 * @author: KyLee
 * @create: 2018-05-04 11:36
 **/
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    //类型位 问题 1
    private static final int QUESTION_TYPE = 1;

    //评论可见状态位 0
    private static final int COMMENT_VAILD = 0;

    @Autowired
    CommentService commentService;
    @Autowired
    TokenHolder tokenHolder;
    @Autowired
    QuestionService questionService;


    /**
     * @description: 增加评论
     * @param model
     * @param content
     * @param questionId
     * @return /question/questionId (位于QuestionController)
     */
    @RequestMapping(value = "/addComment"   , method = {RequestMethod.POST})
     String addComment(Model model,
                   @RequestParam("content") String content,
                   @RequestParam("questionId") int questionId){
        try {
            //int qid =Integer.parseInt(questionId);
            Comment comment = new Comment();
            comment.setEntityId(questionId);
            comment.setEntityType(QUESTION_TYPE);
            if (tokenHolder.getUser() == null) {
                model.addAttribute("msg", "您需要登录才能评论");
                return "redirect:/reglogin";
            } else {
                comment.setUserId(tokenHolder.getUser().getId());
            }
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            comment.setContent(content);

            commentService.addComment(comment);

            //修改问题的评论总数
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);
        }
        catch(Exception e){
            logger.error("添加评论失败"+e.getMessage());
        }
        return "redirect:/question/"+String.valueOf(questionId);
    }

}
