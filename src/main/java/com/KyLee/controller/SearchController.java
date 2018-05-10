package com.KyLee.controller;

import com.KyLee.event.EventModel;
import com.KyLee.event.EventProducer;
import com.KyLee.event.EventType;
import com.KyLee.model.Comment;
import com.KyLee.model.Question;
import com.KyLee.model.TokenHolder;
import com.KyLee.model.ViewObject;
import com.KyLee.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.KyLee.model.StatusWord.COMMENT_VAILD;
import static com.KyLee.model.StatusWord.ENTITY_TYPE_QUESTION;

/**
 * @program: zhihu0.1
 * @description: 搜索C端
 * @author: KyLee
 * @create: 2018-05-04 11:36
 **/
@Controller
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);



    @Autowired
    CommentService commentService;
    @Autowired
    TokenHolder tokenHolder;
    @Autowired
    QuestionService questionService;
    @Autowired
    SearchService searchService;
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;
    /**
     * @description: 添加对问题的评论
     *               前端文件：popup.js
     *               前端链接：/addComment
     *               前端传入：int questionId ,String context
     *
     *               中间操作：修改问题的评论总数
     *
     *               后端返回：问题详情页
     * @param model
     * @return "redirect:/question/" + questionId
     */
    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String search(Model model, @RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "10") int count) {
        try {

            //这里的question只有title和content 和 ID
            List<Question> questionList = searchService.searchQuestion(keyword, offset, count,
                    "<font color=\"red\">", "</font>");
            List<ViewObject> vos = new ArrayList<>();
            for (Question question : questionList) {
                Question q = questionService.getQuestionById(question.getId());
                ViewObject vo = new ViewObject();
                if (question.getContent() != null) {
                    q.setContent(question.getContent());
                }
                if (question.getTitle() != null) {
                    q.setTitle(question.getTitle());
                }
                vo.set("question", q);
                vo.set("followCount", followService.getFollowerCount(ENTITY_TYPE_QUESTION, question.getId()));
                vo.set("user", userService.getUser(q.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            logger.error("搜索评论失败" + e.getMessage());
        }
        return "result";
    }
}
