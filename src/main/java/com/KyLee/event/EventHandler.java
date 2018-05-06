package com.KyLee.event;

import java.util.List;

/**
 * @program: zhihu0.1
 * @description: 事件处理者 接口
 * @author: KyLee
 * @create: 2018-05-06 16:14
 **/

public interface EventHandler {
    void doHandler(EventModel eventModel);

    List<EventType> getRelevanceWithEventType();
}
