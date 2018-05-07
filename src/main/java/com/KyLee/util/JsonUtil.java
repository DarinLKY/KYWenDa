package com.KyLee.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @program: zhihu0.1
 * @description: JSON工具类
 * @author: KyLee
 * @create: 2018-05-03 17:19
 **/
public class JsonUtil {
        public static String getJSONString(int code, String msg){
            JSONObject json = new JSONObject();
            json.put("code",code);
            json.put("msg",msg);
            return json.toString();
        }
    public static String getJSONString(int code){
        JSONObject json = new JSONObject();
        json.put("code",code);
        return json.toString();
    }
    public static String getJSONString(int code, Map<String ,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        for (Map.Entry<String, Object> entry:map.entrySet()){
            json.put(entry.getKey(),entry.getValue());
        }
        return json.toString();
    }
}
