package com.KyLee.service;

import com.KyLee.dao.QuestionDAO;
import com.KyLee.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @program: zhihu0.1
 * @description: 问题主体服务处理
 * @author: KyLee
 * @create: 2018-05-01 19:21
 **/
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveWordService sensitiveWordService;

    public List<Question> getLatestQuestionsByUserId(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestionsByUserId(userId, offset, limit);
    }

    public int AddQuestion(Question question){
        //HTML语言转义
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));

        //过滤
        question.setTitle(sensitiveWordService.filter(question.getTitle()));
        question.setContent(sensitiveWordService.filter(question.getContent()));

        return (questionDAO.addQuestion(question)>0?question.getId():0);
    }
    public Question getQuestionById(int questionId){
        return questionDAO.selectById(questionId);
    }
    public void  updateCommentCount(int id, int count){
        questionDAO.updateCommentCount(id ,count);

    }
}
