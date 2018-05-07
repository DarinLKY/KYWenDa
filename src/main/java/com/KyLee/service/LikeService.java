package com.KyLee.service;

import com.KyLee.util.JedisBackend;
import com.KyLee.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: zhihu0.1
 * @description: 赞踩服务
 * @author: KyLee
 * @create: 2018-05-05 17:07
 **/

@Service
public class LikeService {

    @Autowired
    JedisBackend jedisBackend;


    public long getLikeCount(int entityId,int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        long count = jedisBackend.scard(likeKey);
        return count;
    }

    public long getDislikeCount(int entityId,int entityType){
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        long count = jedisBackend.scard(disLikeKey);
        return count;
    }

    public long like(int userId,int entityId,int entityType){
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);

        //先删除踩数，后添加赞数
        jedisBackend.srem(disLikeKey,String.valueOf(userId));
        return jedisBackend.sadd(likeKey,String.valueOf(userId));
    }

    public long dislike(int userId,int entityId,int entityType){
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);

        //先删除赞数，后添加踩数
        jedisBackend.srem(likeKey,String.valueOf(userId));
        return jedisBackend.sadd(dislikeKey,String.valueOf(userId));
    }

    //得到用户赞踩状态。

    /**
     *
     * @param userId
     * @param entityId
     * @param entityType
     * @return 1  已赞
     *         0  无
     *         -1 已踩
     */
    public long getLikeStatus(int userId,int entityId,int entityType){
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);

        if(jedisBackend.sismember(likeKey,String.valueOf(userId)))return 1;
        else if(jedisBackend.sismember(disLikeKey,String.valueOf(userId)))return -1;
        else return 0;
    }

}
