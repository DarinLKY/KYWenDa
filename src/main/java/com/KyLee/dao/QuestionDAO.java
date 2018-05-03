package com.KyLee.dao;

import com.KyLee.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    String selectFields = " title ,content,user_id,created_date,comment_count ";
    String insertFields = " id ,title ,content,user_id,created_date,comment_count ";
    // #{} 表示bean中的字段，需要与设置的字段名相同。
    @Insert({"insert into "
            ,tableName,
            "(",
            selectFields,
            ") values(#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select * from " +tableName+" where user_id=#{id}"})
    Question selectByUserId(int id);

    List<Question> selectLatestQuestionsByUserId(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);

}
