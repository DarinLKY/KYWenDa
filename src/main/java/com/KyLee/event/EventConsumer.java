package com.KyLee.event;

import com.KyLee.util.JedisBackend;
import com.KyLee.util.RedisKeyUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @program: zhihu0.1
 * @description: 事件消费者
 * @author: KyLee
 * @create: 2018-05-06 16:14
 **/

@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    /*实际上这是一个多对多的关系，type对应HandlerList（整个任务所需的handler）  handler对应多个type（handler可以处理任务的部分或全部）

     config的功能就是从  handler对应多个type 的关系中 变为  type对应HandlerList 的hashMap */
    private static Map<EventType,List<EventHandler>> config = new Hashtable<EventType,List<EventHandler>>();

    private ApplicationContext applicationContext;
    @Autowired
    JedisBackend jedisBackend;


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        //以下把所有的handler type 注册到config.
        if(beans!=null){
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {

                //types得到当前Handler(entry.getValue())所得到的所有关联type，
                EventHandler eventHandler = entry.getValue();
                List<EventType> types = eventHandler.getRelevanceWithEventType();

                for (EventType type :types){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(eventHandler);
                }
            }
        }

        //%%%可以改为使用线程池，并且可以把每个事件类型使用一个队列。%%%
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String key = RedisKeyUtil.getEventQueueKey();

                    //弹出了(key,value)
                    List<String> events = jedisBackend.brpop(0, key);

                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }

                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getEventType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }
                        List<EventHandler> handlers = config.get(eventModel.getEventType());
                        for (EventHandler handler : handlers) {
                            handler.doHandler(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
