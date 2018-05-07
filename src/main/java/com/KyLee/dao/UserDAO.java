package com.KyLee.dao;

import com.KyLee.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface UserDAO {
     String tableName="user";
     String selectFields = " id,name,password,salt,email,head_url";
     String insertFields = " name,password,salt,email, head_url";
    // #{} 表示bean中的字段，需要与设置的字段名相同。
    @Insert({"insert into "
            ," user ",
            "(",
            insertFields,
            ") values(#{name},#{password},#{salt},#{email},#{headUrl})"})
    int addUser(User user);

    @Select({"select * from " +tableName+" where name=#{name}"})
    User selectByName(String name);

    @Select({"select ", selectFields, " from ", tableName, " where id=#{id}"})
    User selectById(int id);

    //@Param 为传到XML中的变量名，应用于复杂逻辑sql语句。
    ArrayList<User> selectLatestUsers(@Param("offset") int offset,
                                      @Param("limit") int limit);
}
