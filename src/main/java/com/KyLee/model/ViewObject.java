package com.KyLee.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: zhihu0.1
 * @description: 用于页面上问题对象的导入
 * @author: KyLee
 * @create: 2018-05-01 19:18
 **/


    public class ViewObject {
        private Map<String, Object> objs = new HashMap<String, Object>();
        public void set(String key, Object value) {
            objs.put(key, value);
        }

        public Object get(String key) {
            return objs.get(key);
        }
    }
