package com.KyLee.dao;

import com.KyLee.model.Comment;
import com.KyLee.model.Feed;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @program: zhihu0.1
 * @description: feeddao
 * @author: KyLee
 * @create: 2018-05-08 19:45
 **/
@Mapper
public interface FeedDAO {
    String tableName=" feed ";
    String selectFields = " id  ,user_id ,type,created_date,data ";
    String insertFields = "  user_id ,type ,created_date,data ";

    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
    Feed selectFeedById(int id);

    List<Feed> selectUserFeeds(@Param("maxId") int maxId, @Param("userIds") List<Integer> userIds,
                               @Param("offset") int offset,@Param("limit") int limit);

    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values( #{userId} ,#{type} ,#{createdDate},#{data})"})
    int addFeed(Feed feed);
    @Select({"select ", selectFields, " from ", tableName, " where user_id=#{userId}"})
    Feed selectFeedByUserId(int userId);

}
