package com.KyLee.event.handler;

import com.KyLee.event.EventHandler;
import com.KyLee.event.EventModel;
import com.KyLee.event.EventType;
import com.KyLee.model.*;
import com.KyLee.service.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.KyLee.model.StatusWord.*;

/**
 * @program: zhihu0.1
 * @description: 添加feed流，每当有对应时间发生则添加feed流。并推送到每个粉丝的timeline
 * @author: KyLee
 * @create: 2018-05-07 23:31
 **/
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;
    @Autowired
    TokenHolder tokenHolder;
    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    private String bulidFeedJsonData( EventModel eventModel){
        Map<String ,String > map = new Hashtable<String ,String>();
        User actor = userService.getUser(eventModel.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());
        if((eventModel.getEventType()==EventType.FOLLOW &&
                eventModel.getEntityType()==ENTITY_TYPE_QUESTION)){
            Question question = questionService.getQuestionById(eventModel.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());

            return JSONObject.toJSONString(map);
        }
        if(eventModel.getEventType()==EventType.COMMENT){
            Question question = questionService.getQuestionById(eventModel.getEntityId());
            //Comment comment = commentService.getComment((int)eventModel.getOthers().get("commentId"));
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
           /* if(comment != null){
                map.put("commentContent",comment.getContent());
            }
            */
            return JSONObject.toJSONString(map);
        }
        return null;
    }
    @Override
    public void doHandler(EventModel eventModel) {
        Feed feed =new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(eventModel.getActorId());
        feed.setType(eventModel.getEventType().getType());
        feed.setData(bulidFeedJsonData(eventModel));
        if (feed.getData() == null) {
            return;
        }
        feedService.addFeed(feed);

        //推送给所有粉丝 这里可以做优化。对于少部分不活跃的用户就不推送
        List<Integer> followerIds = followService.getFollowerIds(eventModel.getActorId(),ENTITY_TYPE_USER,0,Integer.MAX_VALUE);
        for(Integer followerId: followerIds){
            feedService.pushFeedId(followerId,feed.getId());
        }

    }

    @Override
    public List<EventType> getRelevanceWithEventType() {
            return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
