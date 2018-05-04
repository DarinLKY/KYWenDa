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



    @RequestMapping(value = "/question/{questionId}", method = {RequestMethod.GET})
    public String addQusetion(Model model, @PathVariable("questionId") int questionId) {
        Question question = questionService.getQuestionById(questionId);
        model.addAttribute("question",question);

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
