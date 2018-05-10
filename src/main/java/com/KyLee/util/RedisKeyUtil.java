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
    private static final String EVENT_QUEUE_KEY = "event_queue";
    //某个类型的 关注者
    private static final String FOLLOWER_KEY = "follower";
    //某个类型的 所有关注的对象
    private static final String FOLLOWEE_KEY = "followee";
    private static String TIMELINE_KEY = "timeline";


    //返回键值为 like:id:type
    public static String getLikeKey (int entity_id,int entity_type){
        return LIKE_KEY + SPILT_WORD+String.valueOf(entity_id)+SPILT_WORD+String.valueOf(entity_type);
    }

    //返回键值为 dislike:id:type
    public static String getDislikeKey (int entity_id,int entity_type){
        return DISLIKE_KEY + SPILT_WORD+String.valueOf(entity_id)+SPILT_WORD+String.valueOf(entity_type);
    }

    //返回键值为 事务关键词
    public static String getEventQueueKey(){
        return EVENT_QUEUE_KEY ;
    }

    //返回键值为 follower:entity_id:id:entity_type
    // values代表 （类型为entity_type，ID为entity_id对象） 的关注者。
    public static String getFollowerKey (int entity_id,int entity_type){
        return FOLLOWER_KEY+ SPILT_WORD+String.valueOf(entity_id)+SPILT_WORD+String.valueOf(entity_type);
    }

    //返回键值为 followee:userId:entity_type , 因为 能做出关注动作的只有用户，所以key的发起者只有用户。
    //values代表 （userId关注的 类型为 entity_type的数据的）列表。
    public static String getFolloweeKey (int userId,int entity_type){
        return FOLLOWEE_KEY+ SPILT_WORD+String.valueOf(userId)+SPILT_WORD+String.valueOf(entity_type);
    }

    public static String getTimelineKey(int userId){
        return TIMELINE_KEY+SPILT_WORD+String.valueOf(userId);
    }





}
