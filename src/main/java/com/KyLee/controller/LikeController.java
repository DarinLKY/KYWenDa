package com.KyLee.controller;

import com.KyLee.event.EventModel;
import com.KyLee.event.EventProducer;
import com.KyLee.event.EventType;
import com.KyLee.model.Comment;
import com.KyLee.model.Message;
import com.KyLee.model.TokenHolder;
import com.KyLee.model.User;
import com.KyLee.service.CommentService;
import com.KyLee.service.LikeService;
import com.KyLee.util.JedisBackend;
import com.KyLee.util.JsonUtil;
import com.KyLee.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
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

import static com.KyLee.model.StatusWord.ENTITY_TYPE_COMMENT;
import static com.KyLee.model.StatusWord.ENTITY_TYPE_QUESTION;

/**
 * @program: zhihu0.1
 * @description: 赞踩C端
 * @author: KyLee
 * @create: 2018-05-05 17:54
 **/
@Controller
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    LikeService likeService;
    @Autowired
    TokenHolder tokenHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;
    /**
     * @description: 添加评论
     *               前端文件：action.js
     *               前端链接：/like
     *               前端传入：int commentId
     *
     *               中间操作：发送站内信给评论的作者
     *
     *               后端载入：JSON ->(code,msg)
     *                       (code=0 msg=likeCount) 成功
     *                       code=1 失败
     *                       code=999 需要登录
     * @param model
     * @param commentId
     * @return JSON
     */

    @RequestMapping(value = "/like" , method = {RequestMethod.POST})
    @ResponseBody
    String like(Model model,
                      @RequestParam("commentId") int commentId){
        try{
            if (tokenHolder.getUser() == null) {
                return JsonUtil.getJSONString(999);
            }
            //如果已经点过赞，直接返回，不做处理
            if(likeService.getLikeStatus(tokenHolder.getUser().getId(),commentId,ENTITY_TYPE_COMMENT)>0){
                return JsonUtil.getJSONString(0,String.valueOf(likeService.getLikeCount(commentId,ENTITY_TYPE_COMMENT)));
            }

            likeService.like(tokenHolder.getUser().getId(),commentId,ENTITY_TYPE_COMMENT);

            Comment comment = commentService.getComment(commentId);

            //发送站内信。
            EventModel em = new EventModel().setEventType(EventType.LIKE)
                    .setActorId(tokenHolder.getUser().getId())
                    .setEntityId(commentId)
                    .setEntityType(ENTITY_TYPE_COMMENT)
                    .setOwnerId(comment.getUserId())
                    .setOthers("questionId",comment.getEntityId());
            //自己给自己点赞不需要通知。
            if(tokenHolder.getUser().getId()!=comment.getUserId()) {
                eventProducer.fireEvent(em);
            }

            //返回赞数
            return JsonUtil.getJSONString(0,String.valueOf(likeService.getLikeCount(commentId,ENTITY_TYPE_COMMENT)));
        }catch (Exception e){
            logger.error("点赞失败"+e.getMessage());
        }
        return JsonUtil.getJSONString(1,"点赞失败");
    }

    /**
     * @description: 添加评论
     *               前端文件：action.js
     *               前端链接：/dislike
     *               前端传入：int commentId
     *
     *               中间操作：删除发送的站内信
     *
     *               后端载入：JSON ->(code,msg)
     *                       (code=0 msg=likeCount) 成功
     *                       code=1 失败
     *                       code=999 需要登录
     * @param model
     * @param commentId
     * @return JSON
     */
    @RequestMapping(value = "/dislike"   , method = {RequestMethod.POST})
    @ResponseBody
    String dislike(Model model,
                      @RequestParam("commentId") int commentId){
        try {
            if (tokenHolder.getUser() == null) {
                return JsonUtil.getJSONString(999);
            }

            //如果已踩。直接返回，不做处理
            if(likeService.getLikeStatus(tokenHolder.getUser().getId(),commentId,ENTITY_TYPE_COMMENT)<0){
                return JsonUtil.getJSONString(0,String.valueOf(likeService.getLikeCount(commentId,ENTITY_TYPE_COMMENT)));
            }
            likeService.dislike(tokenHolder.getUser().getId(), commentId, ENTITY_TYPE_COMMENT);

            Comment comment = commentService.getComment(commentId);
            EventModel em = new EventModel().setEventType(EventType.DISLIKE)
                    .setActorId(tokenHolder.getUser().getId())
                    .setEntityId(commentId)
                    .setEntityType(ENTITY_TYPE_COMMENT)
                    .setOwnerId(comment.getUserId())
                    .setOthers("questionId",comment.getEntityId());
            eventProducer.fireEvent(em);

            return JsonUtil.getJSONString(0, String.valueOf(likeService.getLikeCount(commentId, ENTITY_TYPE_COMMENT)));
        }catch (Exception e){
            logger.error("点踩失败"+e.getMessage());
        }
        return JsonUtil.getJSONString(1,"点踩失败");
    }

}
