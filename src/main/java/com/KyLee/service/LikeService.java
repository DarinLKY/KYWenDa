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
        long count = jedisBackend.getSetCount(likeKey);
        return count;
    }

    public long getDislikeCount(int entityId,int entityType){
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        long count = jedisBackend.getSetCount(disLikeKey);
        return count;
    }

    public long like(int userId,int entityId,int entityType){
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);

        //先删除踩数，后添加赞数
        jedisBackend.removeSet(disLikeKey,String.valueOf(userId));
        return jedisBackend.addSet(likeKey,String.valueOf(userId));
    }

    public long dislike(int userId,int entityId,int entityType){
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);

        //先删除赞数，后添加踩数
        jedisBackend.removeSet(likeKey,String.valueOf(userId));
        return jedisBackend.addSet(dislikeKey,String.valueOf(userId));
    }

    //得到用户赞踩状态。
    public long getLikeStatus(int userId,int entityId,int entityType){
        String disLikeKey = RedisKeyUtil.getDislikeKey(entityId,entityType);
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);

        if(jedisBackend.isSetMember(likeKey,String.valueOf(userId)))return 1;
        else if(jedisBackend.isSetMember(disLikeKey,String.valueOf(userId)))return -1;
        else return 0;
    }

}
