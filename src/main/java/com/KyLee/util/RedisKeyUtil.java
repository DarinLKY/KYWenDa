package com.KyLee.util;

/**
 * @program: zhihu0.1
 * @description: Redis数据库 业务键值的统一处理
 * @author: KyLee
 * @create: 2018-05-05 17:00
 **/
public class RedisKeyUtil {
    private static final String SPILT_WORD = ":";

    private static final String LIKE_KEY = "like";
    private static final String DISLIKE_KEY = "dislike";

    //返回键值为 Like:id:type
    public static String getLikeKey (int entity_id,int entity_type){
        return LIKE_KEY + SPILT_WORD+String.valueOf(entity_id)+SPILT_WORD+String.valueOf(entity_type);
    }

    //返回键值为 Dislike:id:type
    public static String getDislikeKey (int entity_id,int entity_type){
        return DISLIKE_KEY + SPILT_WORD+String.valueOf(entity_id)+SPILT_WORD+String.valueOf(entity_type);
    }
}
