package com.KyLee.controller;


import com.KyLee.event.EventModel;
import com.KyLee.event.EventProducer;
import com.KyLee.event.EventType;
import com.KyLee.model.Question;
import com.KyLee.model.TokenHolder;
import com.KyLee.model.User;
import com.KyLee.model.ViewObject;
import com.KyLee.service.*;
import com.KyLee.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.KyLee.model.StatusWord.ENTITY_TYPE_QUESTION;
import static com.KyLee.model.StatusWord.ENTITY_TYPE_USER;

/**
 * @program: zhihu0.1
 * @description: 关注与被关注处理
 * @author: KyLee
 * @create: 2018-05-07 13:40
 **/
@Controller
public class FollowerController {
    private static final Logger logger = LoggerFactory.getLogger(FollowerController.class);

    @Autowired(required = false)
    MessageService messageService;
    @Autowired
    CommentService commentService;
    @Autowired(required = false)
    UserService userService;

    @Autowired
    TokenHolder tokenHolder;

    @Autowired
    FollowService followService;
    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    /**
     * @description: 关注用户
     *               前端文件：follow.js
     *               前端链接：/followUser
     *               前端传入：int userId
     *
     *               中间操作：发送FOLLOW事务，发送站内信给被关注用户
     *
     *               后端载入：JSON ->(code,followerCount)
     *                       code与当前用户关注的人的总数
     *                       code=0 成功
     *                       code=1 失败
     *
     * @param userId
     * @return JSON ->(code,followerCount)
     */
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        User localUser = tokenHolder.getUser();
        if(localUser == null){
            return JsonUtil.getJSONString(999);
        }

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setOwnerId(userId)
                .setActorId(localUser.getId())
                .setEntityType(ENTITY_TYPE_USER)
                .setEntityId(userId));

       boolean res =  followService.follow(localUser.getId(),userId,ENTITY_TYPE_USER);
        // 返回当前用户 关注的人数
        return JsonUtil.getJSONString(res == true ? 0:1,String.valueOf(followService.getFolloweeCount(localUser.getId(),ENTITY_TYPE_USER)));
    }

    /**
     * @description: 取消关注用户
     *               前端文件：follow.js
     *               前端链接：/unfollowUser
     *               前端传入：int userId
     *
     *               中间操作：发送UNFOLLOW事务撤回消息
     *
     *               后端载入：JSON ->(code,followerCount)
     *                       code与当前用户关注的人的总数
     *                       code=0 成功
     *                       code=1 失败
     *
     * @param userId
     * @return JSON ->(code,followerCount)
     */
    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        User localUser = tokenHolder.getUser();
        if(localUser == null){
            return JsonUtil.getJSONString(999);
        }
        boolean res =  followService.unFollow(localUser.getId(),userId,ENTITY_TYPE_USER);

        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setOwnerId(userId)
                .setActorId(localUser.getId())
                .setEntityType(ENTITY_TYPE_USER)
                .setEntityId(userId));
        return JsonUtil.getJSONString(res == true ? 0:1,String.valueOf(followService.getFolloweeCount(localUser.getId(),ENTITY_TYPE_USER)));
    }

    /**
     * @description: 关注问题
     *               前端文件：follow.js
     *               前端链接：/followQuestion
     *               前端传入：int userId
     *
     *               中间操作：发送FOLLOW事务，发送站内信给问题的作者用户
     *
     *               后端载入：JSON ->(code,info)
     *                       code与 Map<String, Object> info
     *                       info->("name" : String localUserName,
     *                              "id" : int localUserId,
     *                              "count" : int QuestionFollowerCount)
     *
     * @param questionId
     * @return JSON ->(code,info)
     */
    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        User localUser = tokenHolder.getUser();
        if(localUser == null){
            return JsonUtil.getJSONString(999);
        }

        boolean res =  followService.follow(localUser.getId(),questionId,ENTITY_TYPE_QUESTION);

        Question question = questionService.getQuestionById(questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setOwnerId(questionId)
                .setActorId(localUser.getId())
                .setEntityType(ENTITY_TYPE_QUESTION)
                .setEntityId(question.getUserId()));
        Map<String, Object> info = new Hashtable<String, Object>();

        //写入JSON信息。
        //info.put("headUrl", localUser.getHeadUrl());
        info.put("name", localUser.getName());
        info.put("id", localUser.getId());
        info.put("count", followService.getFollowerCount( questionId,ENTITY_TYPE_QUESTION));

        return JsonUtil.getJSONString(res == true ? 0:1,info);
    }

    /**
     * @description: 取消关注问题
     *               前端文件：follow.js
     *               前端链接：/unfollowQuestion
     *               前端传入：int questionId
     *
     *               中间操作：发送UNFOLLOW事务，
     *
     *               后端载入：JSON ->(code,info)
     *                       code与 Map<String, Object> info
     *                       info->("id":int localUserId ,"count": int QuestionFollowerCount)
     *
     * @param questionId
     * @return JSON ->(code,info)
     */
    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        User localUser = tokenHolder.getUser();
        if(localUser == null){
            return JsonUtil.getJSONString(999);
        }

        boolean res =  followService.unFollow(localUser.getId(),questionId,ENTITY_TYPE_QUESTION);

        Question question = questionService.getQuestionById(questionId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setOwnerId(questionId)
                .setActorId(localUser.getId())
                .setEntityType(ENTITY_TYPE_QUESTION)
                .setEntityId(question.getUserId()));

        Map<String, Object> info = new Hashtable<String, Object>();
        info.put("id", localUser.getId());
        info.put("count", followService.getFollowerCount(ENTITY_TYPE_QUESTION, questionId));
        return JsonUtil.getJSONString(res == true ? 0:1,info);
    }


    /**
     * @description: 关注者列表
     *               前端文件：.html
     *               前端链接：/user/{uid}/followers
     *               前端传入：int userId
     *
     *               中间操作：
     *
     *               后端载入：(List<ViewObject> followers(所有的关注者)
     *                              , int followerCount
     *                              , User curUser)
     *                       ViewObject -> ( User user,int commentCount,int followerCount,int followeeCount,boolean followed)
     *
     * @param userId
     * @return follower.html
     */
    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {

        User localUser =tokenHolder.getUser();
        long followerCount = followService.getFollowerCount(userId,ENTITY_TYPE_USER);
        List<Integer> followerIds = followService.getFollowers(userId,ENTITY_TYPE_USER,0,(int)followerCount);
        if (localUser != null) {
            model.addAttribute("followers", getUsersInfo(localUser.getId(), followerIds));
        } else {
            return "redirect:/reglogin";
        }

        model.addAttribute("followerCount",followerCount);
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    /**
     * @description: 关注的用户 列表
     *               前端文件：.html
     *               前端链接：/user/{uid}/followees
     *               前端传入：int userId
     *
     *               中间操作：
     *
     *               后端载入：(List<ViewObject> followees(所有的关注者)
     *                              , int followeeCount
     *                              , User curUser)
     *                       ViewObject -> ( User user,int commentCount,int followerCount,int followeeCount,boolean followed)
     *
     * @param userId
     * @return follower.html
     */
    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        User localUser =tokenHolder.getUser();
        long followeeCount = followService.getFolloweeCount(userId,ENTITY_TYPE_USER);
        List<Integer> followeeIds = followService.getFollowees(userId,ENTITY_TYPE_USER,0,(int)followeeCount);
        if (localUser != null) {
            model.addAttribute("followees", getUsersInfo(localUser.getId(), followeeIds));
        } else {
            return "redirect:/reglogin";
        }
        model.addAttribute("followeeCount",followeeCount);
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    //所在用户的个人简要信息
    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getCommentsByUserId(user.getId()).size());
            vo.set("followerCount", followService.getFollowerCount(uid,ENTITY_TYPE_USER));
            vo.set("followeeCount", followService.getFolloweeCount(uid, ENTITY_TYPE_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollowerMember(uid, ENTITY_TYPE_USER, localUserId));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
