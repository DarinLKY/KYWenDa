package com.KyLee.controller;

import com.KyLee.model.Question;
import com.KyLee.model.User;
import com.KyLee.model.ViewObject;
import com.KyLee.service.QuestionService;
import com.KyLee.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: zhihu0.1
 * @description: 测试网页。
 * @author: KyLee
 * @create: 2018-04-26 14:21
 **/

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    /*
    @RequestMapping(path={"/"})
    @ResponseBody
    public String request (Model model,
                           HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession session
                           //@PathVariable("userid") int userid ,
                           //@RequestParam("key")String key,
                           //@RequestParam("re") String re
                           ){
        return "hello,spring!";
    }
    */

    /**
     * @description: 通过用户ID 得到问题
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    private List<ViewObject> getQuestionsByUserId(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestionsByUserId(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }


    /**
     * @description: 得到用户ID提出的问题页。
     * @param model
     * @param userId
     * @return shouye.html
     */
    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.POST,RequestMethod.GET})
    public String home_index (Model model,
                        @PathVariable("userId") int userId){
        model.addAttribute("vos", getQuestionsByUserId(userId, 0, 10));
        return "shouye";
    }

    /**
     * @description: 默认显示首页，UserId设为0。
     * @param model
     * @return shouye.html
     */
    @RequestMapping(path={"/"},method = {RequestMethod.POST,RequestMethod.GET})
    public String home (Model model){
        model.addAttribute("vos", getQuestionsByUserId(0,0,10));
        return "shouye";
    }




}
