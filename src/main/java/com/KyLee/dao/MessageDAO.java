package com.KyLee.dao;

import com.KyLee.model.Message;
import com.KyLee.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @program: zhihu0.1
 * @description: 消息dao层
 * @author: KyLee
 * @create: 2018-05-04 17:13
 **/
@Mapper
public interface MessageDAO {

    String tableName=" message ";
    String selectFields = " id , from_id , to_id ,content,created_date,has_read,conversation_id ";
    String insertFields = " from_id , to_id ,content,created_date,has_read,conversation_id ";

    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select * from " +tableName+" where conversation_id=#{conversationId} order by created_date desc"})
    List<Message> selectByCoversationId(@Param("conversationId") String conversationId);

    //select from_id, to_id, content, has_read, conversation_id, created_date, count(id) as ac  from
//(select * from message  where to_id = 13 or from_id =13 order by id desc) as tt
// group by conversation_id order by created_date desc limit 0, 10

    //此处使用原本信息的id位传入消息数。
    @Select({"select "+insertFields+", count(*) as id " +
            " from (select * from "+tableName+" where to_id=#{userId} or from_id=#{userId} order by id desc) newtable " +
            " group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> selectConversationList(@Param("userId") int userId,
                                         @Param("offset") int offset, @Param("limit") int limit);

    //取得对话中的未读消息数量
    @Select({"select count(*) from " +tableName+" where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int  selectCoversationUnreadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);

    @Update({"update ", tableName, " set has_read=1 where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    void  updateCoversationHasRead(@Param("userId") int userId,@Param("conversationId") String conversationId);

    @Select({"select * from " +tableName+" where  content=#{content} and conversation_id=#{conversationId}"})
    List<Message>  selectMessageByConverAndContent(@Param("conversationId") String conversationId,@Param("content")String content);

    @Delete({"delete  from " +tableName+" where  id=#{id}"})
    boolean  deleteById(@Param("id") int id);
}
