package com.KyLee.service;

import com.KyLee.dao.FeedDAO;
import com.KyLee.model.Feed;
import com.KyLee.util.JedisBackend;
import com.KyLee.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: zhihu0.1
 * @description: Feed流
 * @author: KyLee
 * @create: 2018-05-08 19:22
 **/
@Service
public class FeedService {

    @Autowired
    JedisBackend jedisBackend;

    @Autowired(required = false)
    FeedDAO feedDAO;

    //拉模式
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int offset , int limit){
        return feedDAO.selectUserFeeds(maxId,userIds,offset,limit);
    }

    public Feed getFeedById(int id){
        return feedDAO.selectFeedById(id);
    }
    public int addFeed(Feed feed){
        return feedDAO.addFeed(feed);
    }

    public Feed getFeedByUserId(int userId){
        return feedDAO.selectFeedById(userId);
    }

    public long pushFeedId(int userId, int feedId){
        String key = RedisKeyUtil.getTimelineKey(userId);
        return jedisBackend.lpush(key,String.valueOf(feedId));
    }

    public List<Integer> getFeedsId(int userId, int start,int end){
        String key = RedisKeyUtil.getTimelineKey(userId);
        List<String> list = jedisBackend.lrange(key,start,end);
        List<Integer> res =new ArrayList<>();
        for (String string :list)
            res.add(Integer.valueOf(string));
        return res;
    }



}
