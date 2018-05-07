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
 * @description: 取消关注事物
 * @author: KyLee
 * @create: 2018-05-08 00:00
 **/
@Component
public class DisFollowHandler implements EventHandler {

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
        List<Message> messages = messageService.getMessageByConverAndContent(message.getConversationId(),message.getContent());
        if(!messages.isEmpty()){
            messageService.deleteById(messages.get(0).getId());
        }
    }

    @Override
    public List<EventType> getRelevanceWithEventType() {
        return Arrays.asList(EventType.UNFOLLOW);
    }
}

