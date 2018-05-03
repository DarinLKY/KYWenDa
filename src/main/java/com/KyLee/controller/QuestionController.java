package com.KyLee.controller;

import com.KyLee.dao.UserDAO;
import com.KyLee.model.Question;
import com.KyLee.model.TokenHolder;
import com.KyLee.model.User;
import com.KyLee.service.QuestionService;
import com.KyLee.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
    UserDAO userDAO;

    @Autowired
    TokenHolder tokenHolder;

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
        Question question = questionService.selectById(questionId);
        User user = userDAO.selectById(question.getUserId());
        model.addAttribute("question",question);
        //model.addAttribute("user",user);
        return  "detail";
    }
}
