package com.KyLee.controller;

import com.KyLee.event.EventModel;
import com.KyLee.event.EventProducer;
import com.KyLee.event.EventType;
import com.KyLee.model.Comment;
import com.KyLee.model.Feed;
import com.KyLee.model.TokenHolder;
import com.KyLee.service.CommentService;
import com.KyLee.service.FeedService;
import com.KyLee.service.FollowService;
import com.KyLee.service.LikeService;
import com.KyLee.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static com.KyLee.model.StatusWord.ENTITY_TYPE_COMMENT;
import static com.KyLee.model.StatusWord.ENTITY_TYPE_USER;

/**
 * @program: zhihu0.1
 * @description: 赞踩C端
 * @author: KyLee
 * @create: 2018-05-05 17:54
 **/
@Controller
public class FeedController {

    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    LikeService likeService;
    @Autowired
    TokenHolder tokenHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;
    /**
     * @description: 推送Feed
     *               前端文件：action.js
     *               前端链接：/pushfeeds
     *               前端传入：无
     *
     *               中间操作：无
     *
     *               后端载入：List<Feed> feeds
     * @param model
     * @return feeds.html
     */

    @RequestMapping(value = "/pushfeeds" , method = {RequestMethod.POST,RequestMethod.GET})
    String like(Model model){
        try{
            if (tokenHolder.getUser() == null) {
                return "redirect:/reglogin";
            }
            //待做分页功能
            List<Integer> feedIds =feedService.getFeedsId(tokenHolder.getUser().getId(),0,10);
            List<Feed> feeds = new ArrayList<Feed>();
            for (Integer feedId : feedIds) {
                Feed feed = feedService.getFeedById(feedId);
                if (feed != null) {
                    feeds.add(feed);
                }
            }
            model.addAttribute("feeds", feeds);
        }catch (Exception e){
            logger.error("推送失败"+e.getMessage());
        }
        return "feeds";
    }

    /**
     * @description: 拉取feeds
     *               前端文件：action.js
     *               前端链接：/pullfeeds
     *               前端传入：无
     *
     *               中间操作：
     *
     *               后端载入：List<Feed> feeds
     * @param model
     * @return feeds.html
     */
    @RequestMapping(value = "/pullfeeds"   , method = {RequestMethod.POST,RequestMethod.GET})
    String dislike(Model model){
        try {
            if (tokenHolder.getUser() == null) {
                return "redirect:/reglogin";
            }
            List<Integer> followeesIds = followService.getFolloweeIds(tokenHolder.getUser().getId(),ENTITY_TYPE_USER,0,Integer.MAX_VALUE);
            //待做分页功能
            List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followeesIds, 0,10);
            model.addAttribute("feeds", feeds);
        }catch (Exception e){
            logger.error("拉取失败"+e.getMessage());
        }
        return "feeds";
    }

}
