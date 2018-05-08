package com.KyLee.controller;

import com.KyLee.event.EventModel;
import com.KyLee.event.EventProducer;
import com.KyLee.event.EventType;
import com.KyLee.model.Comment;
import com.KyLee.model.TokenHolder;
import com.KyLee.service.CommentService;
import com.KyLee.service.QuestionService;
import com.KyLee.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

import static com.KyLee.model.StatusWord.*;

/**
 * @program: zhihu0.1
 * @description: 评论C端
 * @author: KyLee
 * @create: 2018-05-04 11:36
 **/
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);



    @Autowired
    CommentService commentService;
    @Autowired
    TokenHolder tokenHolder;
    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;
    /**
     * @description: 添加对问题的评论
     *               前端文件：popup.js
     *               前端链接：/addComment
     *               前端传入：int questionId ,String context
     *
     *               中间操作：修改问题的评论总数
     *
     *               后端返回：问题详情页
     * @param model
     * @param questionId
     * @param content
     * @return "redirect:/question/" + questionId
     */
    @RequestMapping(value = "/addComment"   , method = {RequestMethod.POST})
     String addCommentForQuestion(Model model,
                   @RequestParam("content") String content,
                   @RequestParam("questionId") int questionId){
        try {
            //int qid =Integer.parseInt(questionId);
            Comment comment = new Comment();

            comment.setEntityId(questionId);

            //对应的对象是Question，即对问题的回复。
            comment.setEntityType(ENTITY_TYPE_QUESTION);

            if (tokenHolder.getUser() == null) {
                return "redirect:/reglogin";
            } else {
                comment.setUserId(tokenHolder.getUser().getId());
            }
            comment.setCreatedDate(new Date());
            comment.setStatus(COMMENT_VAILD);
            comment.setContent(content);

            //单纯这样返回的永远是1.
            int comment_id=commentService.addComment(comment);

            //修改问题的评论总数
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
                    .setEntityId(questionId).setOthers("commentId",comment_id));
        }
        catch(Exception e){
            logger.error("添加评论失败"+e.getMessage());
        }
        return "redirect:/question/" + questionId;
    }
}
