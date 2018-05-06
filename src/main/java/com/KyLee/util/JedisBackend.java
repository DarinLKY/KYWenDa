package com.KyLee.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @program: zhihu0.1
 * @description: Jedis后台服务接口
 * @author: KyLee
 * @create: 2018-05-05 17:09
 **/
@Service
public class JedisBackend implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisBackend.class);
    private static final String DATABASE ="10";
    private JedisPool jedisPool;
    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("redis://localhost:6379/10");
    }

    //添加集合value
    public long addSet(String key,String value){
        Jedis jedis =null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("添加键值出错"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }
    //删除集合value
    public long removeSet(String key,String value){
        Jedis jedis =null;
        try{
            jedis = jedisPool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("JedisBackEnd删除键值出错"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    //得到value总数
    public long getSetCount(String key){
        Jedis jedis =null;
        try{
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("JedisBackEnd得到键值失败"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }
    //判断value是否在key值得集合内
    public boolean isSetMember(String key,String value){
        Jedis jedis =null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("JedisBackEnd查找失败"+e.getMessage());
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return false;
    }

}
