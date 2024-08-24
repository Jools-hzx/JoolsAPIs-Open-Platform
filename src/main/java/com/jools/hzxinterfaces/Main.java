package com.jools.hzxinterfaces;


import com.jools.joolsclientsdk.client.JoolsHttpClient;
import com.jools.joolsclientsdk.model.User;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/20 20:04
 * @description: TODO
 */
public class Main {

    public static void main(String[] args) {

        /*
         目前版本一: 使用工具类里面自定义好的 secret key 123456
         */
        JoolsHttpClient client = new JoolsHttpClient("jools", "123456");

        client.testGetRequest();
        client.testPostRequest("Jools He");

        User user = new User();
        user.setName("Wakoo");
        client.testModelPost(user);
    }
}
