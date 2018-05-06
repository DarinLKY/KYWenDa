package com.KyLee.controller;

import com.KyLee.model.TokenHolder;
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

import static com.KyLee.model.StatusWord.ENTITY_TYPE_COMMENT;

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
    /**
     * @description: 添加评论
     *               前端文件：action.js
     *               前端链接：/like
     *               前端传入：int commentId
     *
     *               中间操作：
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
            likeService.like(tokenHolder.getUser().getId(),commentId,ENTITY_TYPE_COMMENT);

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
     *               中间操作：
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
            likeService.dislike(tokenHolder.getUser().getId(), commentId, ENTITY_TYPE_COMMENT);

            return JsonUtil.getJSONString(0, String.valueOf(likeService.getLikeCount(commentId, ENTITY_TYPE_COMMENT)));
        }catch (Exception e){
            logger.error("点踩失败"+e.getMessage());
        }
        return JsonUtil.getJSONString(1,"点踩失败");
    }

}
