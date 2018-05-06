package com.KyLee.event;

import com.KyLee.util.JedisBackend;
import com.KyLee.util.RedisKeyUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: zhihu0.1
 * @description: 事件生产者
 * @author: KyLee
 * @create: 2018-05-06 16:12
 **/

@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    JedisBackend jedisBackend;

    public boolean fireEvent(EventModel eventModel){
        try {
            String key = RedisKeyUtil.getEventQueueKey();
            String model = JSONObject.toJSONString(eventModel);
            //从表头插入，这里可以使用优先队列并设置权重规则。
            jedisBackend.lpush(key, model);
            return true;
        }catch (Exception e){
            logger.error("添加任务失败"+e.getMessage());
            return false;
        }
    }
}
