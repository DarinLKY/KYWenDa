package com.KyLee.dao;

import com.KyLee.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @program: zhihu0.1
 * @description: 问题主体dao
 * @author: KyLee
 * @create: 2018-05-01 18:13
 **/
@Mapper
public interface QuestionDAO {
    public String tableName="question";
    String selectFields = " id ,title ,content,user_id,created_date,comment_count ";
    String insertFields = " title ,content,user_id,created_date,comment_count ";
    // #{} 表示bean中的字段，需要与设置的字段名相同。
    @Insert({"insert into "
            ,tableName,
            "(",
            insertFields,
            ") values(#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select * from " +tableName+" where user_id=#{userId}"})
    Question selectByUserId(int userId);

    @Select({"select * from " +tableName+" where id=#{id}"})
    Question selectById(int id);

    List<Question> selectLatestQuestionsByUserId(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);
    @Update({"update ", tableName, " set comment_count=#{count} where id=#{id}"})
    void updateCommentCount(@Param("id") int id, @Param("count") int count);
}
