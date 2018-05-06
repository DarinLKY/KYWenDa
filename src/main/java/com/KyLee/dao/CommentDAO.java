package com.KyLee.dao;

import com.KyLee.model.Comment;
import com.KyLee.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @program: zhihu0.1
 * @description: 评论dao
 * @author: KyLee
 * @create: 2018-05-04 11:13
 **/
@Mapper
public interface CommentDAO {
    String tableName=" comment ";
    String selectFields = " id , content ,user_id ,entity_id,entity_type,status,created_date ";
    String insertFields = "  content ,user_id ,entity_id,entity_type,status,created_date ";

    @Select({"select "+selectFields +" from "+tableName+
            " where entity_id = #{entityId} and entity_type = #{entityType} order By created_date DESC"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(*) from "+tableName+
            " where entity_id = #{entityId} and entity_type = #{entityType}"})
    int selectCountByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values( #{content} ,#{userId} ,#{entityId},#{entityType},#{status},#{createdDate})"})
    int addComment(Comment comment);


    @Update({"update ", tableName, " set status=#{status} where id=#{id}"})
    int  updateStatus(@Param("id") int id, @Param("status") int status);

    @Select({"select * from "+tableName+ " where id = #{id} "})
    Comment selectById(@Param("id") int id);
}
