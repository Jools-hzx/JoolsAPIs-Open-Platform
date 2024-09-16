package com.jools.hzxinterfaces.controller;

import cn.hutool.http.HttpUtil;
import com.jools.joolsclientsdk.model.User;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/20 19:48
 * @description: TODO
 */
@SpringBootTest
public class NameControllerApisTest {

    @Test
    public void testNameApis() {

        //测试 GET
        String res1 = HttpUtil.get("http://localhost:10000/api/name/getName");
        System.out.println("GET 的请求结果为:" + res1);

        //测试 POST
        Map<String, Object> params = new HashMap<>();
        params.put("username", "Jools He");
        String postRes = HttpUtil.post("http://localhost:10000/api/name/getName", params);
        System.out.println("POST 的请求结果为:" + postRes);

        //测试 POST user
        Map<String, Object> urlParams = new HashMap<>();
        User user = new User();
        user.setName("Wakoo");
        urlParams.put("user", user);
        String modelRequestResult = HttpUtil.post("http://localhost:10000/api/name/getName/model", urlParams);

        System.out.println("携带 User 的请求结果:" + modelRequestResult);
    }
}
