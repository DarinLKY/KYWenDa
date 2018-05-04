package com.KyLee.controller;

import com.KyLee.dao.CommentDAO;
import com.KyLee.dao.UserDAO;
import com.KyLee.model.*;
import com.KyLee.service.CommentService;
import com.KyLee.service.QuestionService;
import com.KyLee.service.UserService;
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

    /**
     * @description: 添加问题 /question/add为前端规定的链接，
     *               传送的变量title,content也为前端规定的存在popup.js内。
     * @param model
     * @param title
     * @param content
     * @return JSON JS代码会自动刷新模板
     *         code=0 成功
     *         code=1 失败
     *         code=999 需要登录
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
              return JsonUtil.getJSONString(1, "添加问题失败，您必须登录。");
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
     * @param model
     * @param questionId
     * @return detail.html
     */
    @RequestMapping(value = "/question/{questionId}", method = {RequestMethod.GET})
    public String addQusetion(Model model, @PathVariable("questionId") int questionId) {

        //增加question详情
        Question question = questionService.getQuestionById(questionId);
        model.addAttribute("question",question);

        //增加 此question 关联的 评论与评论用户
        List<ViewObject> comments =new ArrayList<ViewObject>();
        List<Comment>coms=commentService.getComment(questionId,1);
        for (Comment comment:coms){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);

        return  "detail";
    }
}
