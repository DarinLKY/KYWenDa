package com.KyLee.controller;

import com.KyLee.model.*;
import com.KyLee.service.CommentService;
import com.KyLee.service.FollowService;
import com.KyLee.service.QuestionService;
import com.KyLee.service.UserService;
import jdk.nashorn.internal.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.KyLee.model.StatusWord.ENTITY_TYPE_QUESTION;
import static com.KyLee.model.StatusWord.ENTITY_TYPE_USER;

/**
 * @program: zhihu0.1
 * @description: 测试网页。
 * @author: KyLee
 * @create: 2018-04-26 14:21
 **/

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    TokenHolder tokenHolder;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    /**
     * @description: 首页
     *               前端文件：.html
     *               前端链接：/
     *               前端传入：int userId
     *
     *               中间操作：
     *
     *               后端载入：List<ViewObject> vos
     *                       ViewOject -> (Question question , int followCount , User user )
     *
     * @return follower.html
     */
    @RequestMapping(path={"/","/index"},method = {RequestMethod.POST,RequestMethod.GET})
    public String home (Model model){
        model.addAttribute("vos", getQuestionsByUserId(0,0,10));
        return "shouye";
    }

    /**
     * @description: 方法函数 ：通过用户ID 得到用户发布的所有问题。
     * @param userId
     * @param offset
     * @param limit
     * @return List<ViewObject> vos
     *         ViewOject -> (Question question , int followCount , User user )
     */
    private List<ViewObject> getQuestionsByUserId(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestionsByUserId(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followCount",followService.getFollowerCount(question.getId(),ENTITY_TYPE_QUESTION));
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }


    /**
     * @description: 用户主页
     *               前端文件：.html
     *               前端链接：/user/{userId}
     *               前端传入：int userId
     *
     *               中间操作：
     *
     *               后端载入：List<ViewObject> vos(问题的概要) , ViewObject profileUser(用户当前的关注情况)
     *                       profileUser -> (User user ,int commentCount , int followerCount , int followeeCount ,
     *                       boolean followed)
     *                       ViewOject -> (Question question , int followCount , User user )
     *
     * @param userId
     * @return follower.html
     */
    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.POST,RequestMethod.GET})
    public String home_index (Model model,
                        @PathVariable("userId") int userId){

        model.addAttribute("vos", getQuestionsByUserId(userId, 0, 10));

        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getCommentsByUserId(userId).size());
        vo.set("followerCount", followService.getFollowerCount(userId, ENTITY_TYPE_USER));
        vo.set("followeeCount", followService.getFolloweeCount(userId, ENTITY_TYPE_USER));
        if (tokenHolder.getUser() != null) {
            vo.set("followed", followService.isFollowerMember(userId ,ENTITY_TYPE_USER,tokenHolder.getUser().getId()));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }






}
