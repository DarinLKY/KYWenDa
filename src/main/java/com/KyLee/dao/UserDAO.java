package com.KyLee.dao;

import com.KyLee.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface UserDAO {
    public String tableName="user";

    // #{} 表示bean中的字段，需要与设置的字段名相同。
    @Insert({"insert into "
            ," user ",
            "(",
            "id, name ,password,slat,email ",
            ") values(#{id},#{name},#{password},#{slat},#{email})"})
    int addUser(User user);

    @Select({"select id,name from " +tableName+" where name=#{name}"})
    User selectByName(String name);

    //@Param 为传到XML中的变量名，应用于复杂逻辑sql语句。
    ArrayList<User> selectLatestUsers(@Param("offset") int offset,
                                      @Param("limit") int limit);
}
