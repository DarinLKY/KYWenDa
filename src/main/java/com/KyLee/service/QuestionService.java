package com.KyLee.service;

import com.KyLee.dao.QuestionDAO;
import com.KyLee.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Question> getLatestQuestionsByUserId(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestionsByUserId(userId, offset, limit);
    }

}
