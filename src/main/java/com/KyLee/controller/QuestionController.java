package com.KyLee.controller;

import com.KyLee.dao.CommentDAO;
import com.KyLee.dao.UserDAO;
import com.KyLee.model.*;
import com.KyLee.service.*;
import com.KyLee.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.KyLee.model.StatusWord.ENTITY_TYPE_COMMENT;
import static com.KyLee.model.StatusWord.ENTITY_TYPE_QUESTION;

/**
 * @program: zhihu0.1
 * @description: 问题处理Controller
 * @author: KyLee
 * @create: 2018-05-03 17:10
 **/
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired(required = false)
    CommentService commentService;

    @Autowired
    TokenHolder tokenHolder;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    /**
     * @description: 添加问题
     *               前端文件：popup.js
     *               前端链接："/question/add"
     *               前端传入： String title , String content
     *
     *               中间操作：无
     *
     *               后端载入：JSON ->(code,msg)
     *                       code=0 成功
     *                       code=1 失败
     *                       code=999 需要登录
     * @param model
     * @param title
     * @param content
     * @return JSON
     */
    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQusetion(Model model, @RequestParam("title") String title,
                               @RequestParam("content") String content) {
      try {
          Question question = new Question();

          question.setContent(content);
          question.setTitle(title);
          question.setCommentCount(0);
          question.setCreatedDate(new Date());
          if (tokenHolder.getUser() == null)
              return JsonUtil.getJSONString(999, "添加问题失败，您必须登录。");
          else
              question.setUserId(tokenHolder.getUser().getId());
          if (questionService.AddQuestion(question) > 0)
              return JsonUtil.getJSONString(0);
          else
              return JsonUtil.getJSONString(1, "添加问题失败");
      }
      catch (Exception e){
          logger.error("添加问题失败"+e.getMessage());
      }
        return JsonUtil.getJSONString(1, "失败");
    }


    /**
     * @description: 问题详情页
     *               前端文件：shouye.html
     *               前端链接："/question/{questionId}"
     *               前端传入： int questionId
     *
     *               中间操作：无
     *
     *               后端载入：List<ViewObject> comments , List<ViewObject> followUsers , boolean followed
     *               ViewObject -> (User user , Comment comment,int likeCount,int disLikeCount,int liked)
     *               ViewObject -> (String name , String headUrl , )
     * @param model
     * @param questionId
     * @return detail.html
     */
    @RequestMapping(value = "/question/{questionId}", method = {RequestMethod.GET})
    public String qusetionDetail(Model model, @PathVariable("questionId") int questionId) {

        //增加question详情
        Question question = questionService.getQuestionById(questionId);
        model.addAttribute("question",question);
        User user = tokenHolder.getUser();

        //增加 此question 关联的 评论与评论用户
        List<ViewObject> comments =new ArrayList<ViewObject>();
        List<Comment> coms =commentService.getComment(questionId,1);
        for (Comment comment:coms){
            ViewObject vo = new ViewObject();

            if(user==null){
                vo.set("liked",0);
            }
            else{
                vo.set("liked",likeService.getLikeStatus(user.getId(),comment.getId(),ENTITY_TYPE_COMMENT));
            }

            vo.set("dislikeCount",likeService.getDislikeCount(comment.getId(),ENTITY_TYPE_COMMENT));
            vo.set("likeCount",likeService.getLikeCount(comment.getId(),ENTITY_TYPE_COMMENT));
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);

        }
        model.addAttribute("comments",comments);

        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowerIds(questionId,ENTITY_TYPE_QUESTION,0, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (tokenHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollowerMember(questionId, ENTITY_TYPE_QUESTION,tokenHolder.getUser().getId()));
        } else {
            model.addAttribute("followed", false);
        }

        return  "detail";
    }
}
