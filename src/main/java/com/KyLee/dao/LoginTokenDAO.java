package com.KyLee.dao;

import com.KyLee.model.LoginToken;
import com.KyLee.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * @program: zhihu0.1
 * @description: 用户token dao
 * @author: KyLee
 * @create: 2018-05-01 19:01
 **/
public interface LoginTokenDAO {

    public String tableName="login_token";
    String selectFields = " user_id ,expired,status ";
    String insertFields = " id,user_id ,expired,status ";
    // #{} 表示bean中的字段，需要与设置的字段名相同。
    @Insert({"insert into "
            ,tableName,
            "(",
            selectFields,
            ") values(#{user_id},#{expired},#{status})"})
    int addLoginToken(LoginToken loginToken);

    @Select({"select * from " +tableName+" where user_id=#{id}"})
    LoginToken selectByUserId(String id);
}
