package com.KyLee;

import com.KyLee.service.SensitiveWordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @program: zhihu0.1
 * @description: 敏感词测试
 * @author: KyLee
 * @create: 2018-05-03 21:58
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZhihuApplication.class)
public class SensitiveWordTest {
    @Autowired(required = false)
    SensitiveWordService sensitiveWordService;

    @Test
    public void test(){
        String s ="abcdfdsssabbs误解";
        System.out.println(sensitiveWordService.filter(s));
    }
}
