package com.KyLee.service;

import com.KyLee.util.JedisBackend;
import com.KyLee.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.KyLee.model.StatusWord.ENTITY_TYPE_QUESTION;
import static com.KyLee.model.StatusWord.ENTITY_TYPE_USER;

/**
 * @program: zhihu0.1
 * @description: 关注服务
 * @author: KyLee
 * @create: 2018-05-07 12:04
 **/
@Service
public class FollowService {

    @Autowired
    JedisBackend jedisBackend;
    public boolean follow(int userId,int entityId,int entityType){

        //某实体被关注，因为问题不会主体，所以这里的value只可能是User的id
        String followerKey = RedisKeyUtil.getFollowerKey(entityId,entityType);
        //用户关注列表，用户并指定关注的类型。
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId ,entityType);

        Jedis jedis = jedisBackend.getJedis();
        Transaction tx = jedisBackend.multi(jedis);
        // 以时间为score，
        // 被关注者 添加 自己被关注 的 用户id；
        // 关注者 添加 自己已关注的 id
        tx.zadd(followerKey,new Date().getTime(),String.valueOf(userId));
        tx.zadd(followeeKey,new Date().getTime(),String.valueOf(entityId));
        List<Object> ret = jedisBackend.exec(tx, jedis);
        return ret.size()==2 && (long)(ret.get(0))>0 && (long)(ret.get(1))>0;
    }

    public boolean unFollow(int userId,int entityId,int entityType){

        String followerKey = RedisKeyUtil.getFollowerKey(entityId,entityType);
        //用户关注列表，用户并指定关注的类型。
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId ,entityType);

        Jedis jedis = jedisBackend.getJedis();
        Transaction tx = jedisBackend.multi(jedis);

        tx.zrem(followerKey,String.valueOf(userId));
        tx.zrem(followeeKey,String.valueOf(entityId));
        List<Object> ret = jedisBackend.exec(tx, jedis);
        return ret.size()==2 && (long)(ret.get(0))>0 && (long)(ret.get(1))>0;
    }

    public long getFollowerCount(int entityId,int entityType){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId,entityType);
        return jedisBackend.zcard(followerKey);
    }

    //这里的entityType是指关注的类型。
    public long getFolloweeCount(int userId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisBackend.zcard(followeeKey);
    }

    public boolean isFollowerMember(int entityId,int entityType,int userId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId,entityType);
        return jedisBackend.zscore(followerKey,String.valueOf(userId))!=null;
    }

    public boolean isFolloweeMember(int userId,int entityId,int entityType){
        String followerKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisBackend.zscore(followerKey,String.valueOf(entityId))!=null;
    }

    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }
    public List<Integer> getFollowerIds(int entityId,int entityType,int offset,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId,entityType);
        return getIdsFromSet(jedisBackend.zrevrange(followerKey,offset,count));
    }
    public List<Integer> getFolloweeIds(int userId,int entityType,int offset,int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return getIdsFromSet(jedisBackend.zrevrange(followeeKey,offset,count));
    }
}
