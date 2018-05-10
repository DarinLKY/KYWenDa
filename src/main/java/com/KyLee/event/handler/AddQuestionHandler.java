package com.KyLee.event.handler;

import com.KyLee.event.EventHandler;
import com.KyLee.event.EventModel;
import com.KyLee.event.EventType;
import com.KyLee.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nowcoder on 2016/8/28.
 */
@Component
public class AddQuestionHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);
    @Autowired
    SearchService searchService;

    @Override
    public void doHandler(EventModel eventModel) {
        try {
            searchService.indexQuestion(eventModel.getEntityId(),eventModel.getOther("title").toString(), eventModel.getOther("content").toString());
        } catch (Exception e) {
            logger.error("增加题目索引失败");
        }
    }

    @Override
    public List<EventType> getRelevanceWithEventType() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
