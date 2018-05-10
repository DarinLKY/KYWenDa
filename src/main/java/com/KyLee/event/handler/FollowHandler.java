package com.KyLee.event.handler;

import com.KyLee.event.EventHandler;
import com.KyLee.event.EventModel;
import com.KyLee.event.EventType;
import com.KyLee.model.Message;
import com.KyLee.model.User;
import com.KyLee.service.MessageService;
import com.KyLee.service.QuestionService;
import com.KyLee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static com.KyLee.model.StatusWord.ENTITY_TYPE_QUESTION;
import static com.KyLee.model.StatusWord.ENTITY_TYPE_USER;

/**
 * @program: zhihu0.1
 * @description: 关注事件
 * @author: KyLee
 * @create: 2018-05-07 23:31
 **/
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    QuestionService questionService;
    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(eventModel.getActorId());
        message.setToId(eventModel.getOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(eventModel.getActorId());
        if(eventModel.getEntityType()== ENTITY_TYPE_QUESTION){
            message.setContent("用户" + user.getName()
                    + "关注了你的问题,http://127.0.0.1:8080/question/" + eventModel.getEntityId());
        }
        if(eventModel.getEntityType()== ENTITY_TYPE_USER){
            message.setContent("用户" + user.getName()
                    + "关注了你,http://127.0.0.1:8080/user/" + eventModel.getActorId());
        }

        //这里应该修改为系统通知，自己关注的动作不应该被自己发现，除非是类似于广播给粉丝，或者是直接发消息给指定用户。
        //另外关注这个动作没有必要发通知，应该是在自己的问题或关注的问题有了新的回复，或者自己的评论得到了赞
        //赞可以实现为由多少个用户给自己的评论点赞了，而不是每一个用户赞一次都发一次通知。
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getRelevanceWithEventType() {
            return Arrays.asList(EventType.FOLLOW);
    }
}
