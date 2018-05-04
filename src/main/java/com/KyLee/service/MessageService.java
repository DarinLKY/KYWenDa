package com.KyLee.service;

import com.KyLee.dao.MessageDAO;
import com.KyLee.model.Message;
import com.KyLee.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @program: zhihu0.1
 * @description: 消息服务
 * @author: KyLee
 * @create: 2018-05-04 17:33
 **/
@Service
public class MessageService {

    @Autowired
    SensitiveWordService sensitiveWordService;

    @Autowired(required = false)
    MessageDAO messageDAO;
    public int addMessage(Message message){
        //HTML语言转义
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        //过滤
        message.setContent(sensitiveWordService.filter(message.getContent()));

        return (messageDAO.addMessage(message)>0?message.getId():0);
    }

    public List<Message> getMessageByConversationId(String conversationId){
        return messageDAO.selectByCoversationId(conversationId);
    }

    public List<Message> getConversationList(int userId ,int offset,int limit){
        return messageDAO.selectConversationList(userId,offset,limit);
    }
    public int  getCoversationUnread( int userId,  String conversationId){
        return messageDAO.selectCoversationUnreadCount(userId,conversationId);
    }
    public void   updateCoversationHasRead( int userId,  String conversationId){
         messageDAO.updateCoversationHasRead(userId,conversationId);
    }
}
