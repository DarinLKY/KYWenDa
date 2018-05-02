package com.KyLee.dao;

import com.KyLee.model.LoginToken;
import com.KyLee.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * @program: zhihu0.1
 * @description: 用户token dao
 * @author: KyLee
 * @create: 2018-05-01 19:01
 **/
@Mapper
public interface LoginTokenDAO {

     String tableName="login_token";
    String selectFields = " id , user_id ,token_key ,expired,status ";
    String insertFields = " user_id,token_key,expired,status ";
    @Insert({"insert into ",tableName, "(", insertFields, ") values(#{userId},#{tokenKey},#{expired},#{status})"})
    int addLoginToken(LoginToken loginToken);

    @Select({"select * from " +tableName+" where user_id=#{userId}"})
    LoginToken selectByUserId(int userId);

    @Select({"select * from " +tableName+" where token_key=#{token_key}"})
    LoginToken selectByKey(String token_key);

    @Update({"update ", tableName, " set status=#{status} where token_key=#{tokenKey}"})
    void  updateStatus(@Param("tokenKey") String tokenKey, @Param("status") int status);

    @Update({"update ", tableName, " set expired=#{expired} where token_key=#{tokenKey}"})
    void  updateExpired(@Param("tokenKey") String tokenKey, @Param("expired") Date expired);
}
