package com.KyLee.controller;

import com.KyLee.model.Question;
import com.KyLee.model.User;
import com.KyLee.model.ViewObject;
import com.KyLee.service.QuestionService;
import com.KyLee.service.UserService;
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



    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.POST,RequestMethod.GET})
    public String home_index (Model model,
                        @PathVariable("userId") int userId){
        model.addAttribute("vos", getQuestionsByUserId(userId, 0, 10));
        return "shouye";
    }

    /**
     * @description: 如果当前没有登入，则显示首页，UserId设为0。
     * @param model
     * @return
     */
    @RequestMapping(path={"/"},method = {RequestMethod.POST,RequestMethod.GET})
    public String home (Model model){
        model.addAttribute("vos", getQuestionsByUserId(0,0,10));
        return "shouye";
    }




}
