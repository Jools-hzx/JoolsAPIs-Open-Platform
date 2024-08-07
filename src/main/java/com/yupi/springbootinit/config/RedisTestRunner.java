package com.yupi.springbootinit.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/7 0:58
 * @description: TODO
 */
//@Component
public class RedisTestRunner implements CommandLineRunner {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Testing Redis connection...");
        stringRedisTemplate.opsForValue().set("testKey", "testValue");
        String value = stringRedisTemplate.opsForValue().get("testKey");
        System.out.println("Value for 'testKey': " + value);
    }
}
