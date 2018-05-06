package com.KyLee.event;

/**
 * @program: zhihu0.1
 * @description: 事件类型，业务逻辑，以EventId分辨
 * @author: KyLee
 * @create: 2018-05-06 16:15
 **/
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    DISLIKE(4);

    private int type;
    EventType(int type) { this.type = type; }
    public int getType() { return type; }
}
