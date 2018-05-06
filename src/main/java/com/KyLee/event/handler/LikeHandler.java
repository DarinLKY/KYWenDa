package com.KyLee.event.handler;

import com.KyLee.event.EventHandler;
import com.KyLee.event.EventModel;
import com.KyLee.event.EventType;
import com.KyLee.model.Message;
import com.KyLee.model.User;
import com.KyLee.service.MessageService;
import com.KyLee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @program: zhihu0.1
 * @description: 用于对点赞业务的handler,发送
 * @author: KyLee
 * @create: 2018-05-06 17:20
 **/
@Component
public class LikeHandler implements EventHandler{

    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(eventModel.getActorId());
        message.setToId(eventModel.getOwnerId());
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        User user = userService.getUser(eventModel.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了你的评论,http://127.0.0.1:8080/question/" + String.valueOf(eventModel.getOther("questionId")));


        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getRelevanceWithEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
