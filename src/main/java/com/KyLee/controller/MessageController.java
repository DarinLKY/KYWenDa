package com.KyLee.controller;

import com.KyLee.dao.UserDAO;
import com.KyLee.model.*;
import com.KyLee.service.CommentService;
import com.KyLee.service.MessageService;
import com.KyLee.service.QuestionService;
import com.KyLee.service.UserService;
import com.KyLee.util.JsonUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: zhihu0.1
 * @description: 消息C端
 * @author: KyLee
 * @create: 2018-05-04 17:38
 **/
@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired(required = false)
    MessageService messageService;
    @Autowired(required = false)
    UserService userService;

    @Autowired
    TokenHolder tokenHolder;

    /**
     * @description: 添加消息
     *               前端文件：popupMsg.js
     *               前端链接：/msg/addMessage
     *               前端传入：String toName,String context
     *
     *               后端载入：JSON ->(code,msg)
     *                       code=0 成功
     *                       code=1 失败
     *                       code=2 发送接收不能是同一个人
     *                       code=3 用户名不存在
     *                       code=999 需要登录
     * @param model
     * @param toName
     * @param content
     * @return JSON ->(code,msg)
     */
    @RequestMapping(value = "/msg/addMessage", method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(Model model, @RequestParam("toName") String toName,@RequestParam("content") String content) {
        try {
            User user  = tokenHolder.getUser();
            if (user==null){
                return JsonUtil.getJSONString(999,"您需要先登录");
            }
            User toUser = userService.getUser(toName);
            if(toUser == null){
                return JsonUtil.getJSONString(3,"用户名不存在");
            }
            Message message = new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(user.getId());
            message.setHasRead(0);
            message.setToId(toUser.getId());
            if(user.getId()==toUser.getId()){
                return JsonUtil.getJSONString(2,"不能给自己发送信息");
            }
            messageService.addMessage(message);
            return JsonUtil.getJSONString(0);
        }
        catch (Exception e){
            logger.error("添加消息失败"+e.getMessage());
        }
        return JsonUtil.getJSONString(1, "发送信息失败");
    }

    /**
     * @description: 消息列表
     *               前端文件：letter.html
     *               前端链接：/msg/detail?conversationId=
     *               前端传入：String conversationId
     *
     *               中间操作：更新以当前登录用户为接受方，点击的对话内所有信息的未读状态设置为已读 has_read = 1 。
     *
     *               后端载入：List<ViewObject> messages
     *               ViewObject-> (User user ; Message message)
     * @param model
     * @param conversationId
     * @return letterDetail.html
     */
    @RequestMapping(value = "/msg/detail", method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @Param("conversationId") String conversationId) {
        try {

            int localUserId =tokenHolder.getUser().getId();
            List<Message> ms = messageService.getMessageByConversationId(conversationId);
            List<ViewObject> messages =new ArrayList<ViewObject>();
            for (Message message:ms){
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                vo.set("user",userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);

            //限定conversationId，更新所有以当前用户为接受方的信息为已读。
            messageService.updateCoversationHasRead(localUserId,conversationId);

            return "letterDetail";
        }
        catch (Exception e){
            logger.error("拉取对话失败"+e.getMessage());
        }
        return "redirect:/";
    }

    /**
     * @description: 消息列表
     *               前端文件：header.html , home.js
     *               前端链接：/msg/list
     *               前端传入：none
     *
     *               中间操作：无
     *
     *               后端载入：List<ViewObject> conversations
     *               ViewObject-> (User user , Message conversation , int unReadCount)
     * @param model
     * @return letter.html
     */
    @RequestMapping(value = "/msg/list", method = {RequestMethod.GET})
    public String getMessageList(Model model){
        try {
            User user = tokenHolder.getUser();
            int localUserId =tokenHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<ViewObject>();

            //得到和当前登录用户有关的所有对话。
            List<Message> cs = messageService.getConversationList(user.getId(), 0, 10);
            for (Message conversation : cs) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", conversation);

                //这里找到非当前登录用户的引用。
                //用于得到对话信息列表的对象。
                int targetId = conversation.getFromId() == localUserId ? conversation.getToId() : conversation.getFromId();
                vo.set("user", userService.getUser(targetId));
                //得到未读消息。
                vo.set("unReadCount",messageService.getCoversationUnread(localUserId,conversation.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);

            return "letter";
        }catch (Exception e){
            logger.error("拉取消息列表失败"+e.getMessage());
        }
       return "redirect:/";
    }

}
