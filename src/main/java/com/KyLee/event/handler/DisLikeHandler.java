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
 * @description: 用于点踩的处理器，主要是将发送的通知站内信删除
 * @author: KyLee
 * @create: 2018-05-06 19:35
 **/
@Component
public class DisLikeHandler implements EventHandler{
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
        List<Message> messages = messageService.getMessageByConverAndContent(message.getConversationId(),message.getContent());

        //这里是删除一封，因为现阶段只能识别问题详情页，而不能直接导向具体评论。
        if(!messages.isEmpty()){
            messageService.deleteById(messages.get(0).getId());
        }
    }

    @Override
    public List<EventType> getRelevanceWithEventType() {
        return Arrays.asList(EventType.DISLIKE);
    }
}
