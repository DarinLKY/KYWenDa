package com.KyLee.event;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @program: zhihu0.1
 * @description: 事件模型 getter不能随意更改
 * @author: KyLee
 * @create: 2018-05-06 16:12
 **/
public class EventModel {
    private EventType eventType;

    private int entityType;
    private int entityId;

    private int actorId;
    private int ownerId;


    public EventModel(){

    }
    public EventModel(EventType eventType){
        this.eventType=eventType;
    }
    private Map<String, Object> others = new HashMap<String, Object>();

    public Object  getOther(String key) {
        return others.get(key);
    }

    public EventModel setOthers(String key,Object value) {
        others.put(key,value);
        return this;
    }

    public Map<String, Object> getOthers() {
        return others;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public EventModel setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}
