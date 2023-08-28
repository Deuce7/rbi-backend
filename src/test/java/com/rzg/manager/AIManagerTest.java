package com.rzg.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class AIManagerTest {

    @Resource
    private AIManager aiManager;

    @Test
    void doChat(){
        String result = aiManager.doChat(1692138985813487618L, "分析需求：\n" +
                "请帮我分析网站数据\n" +
                "原始数据：\n" +
                "日期,用户数\n" +
                "1号,10\n" +
                "2号,20\n" +
                "3号,30");
        System.out.println(result);
    }

}
