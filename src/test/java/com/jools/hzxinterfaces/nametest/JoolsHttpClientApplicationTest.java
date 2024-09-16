package com.jools.hzxinterfaces.nametest;

import com.jools.joolsclientsdk.client.JoolsHttpClient;
import com.jools.joolsclientsdk.model.User;
import org.junit.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/23 23:35
 * @description: TODO
 */

@SpringBootTest
@EnableConfigurationProperties(JoolsHttpClient.class)
public class JoolsHttpClientApplicationTest {


    @Resource
    private JoolsHttpClient joolsHttpClient;

    @Test
    void contextLoad() {
        joolsHttpClient.testGetRequest();

        joolsHttpClient.testPostRequest("Jools Wakoo");

        User user = new User();
        user.setName("Jools");

        joolsHttpClient.testModelPost(user);
    }

}
