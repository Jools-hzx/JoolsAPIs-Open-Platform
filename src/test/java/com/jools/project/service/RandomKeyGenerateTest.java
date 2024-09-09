package com.jools.project.service;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/26 19:27
 * @description: TODO
 */
public class RandomKeyGenerateTest {
    public static final String KEY_SALT = "jools";

    @Test
    public void test01() {
        // 4. 为其分配 accessKey & secretKey
        // accessKey 按照 盐+用户名+4位随机数字 生成
        String accessKey = DigestUtils.md5DigestAsHex((
                KEY_SALT + "userAccount" +
                RandomUtil.randomNumbers(4)).getBytes()
        );
        // accessKey 按照 盐+用户名+8位随机数字 生成
        String secretKey = DigestUtils.md5DigestAsHex((
                KEY_SALT + "userAccount" +
                RandomUtil.randomNumbers(8)).getBytes()
        );

        System.out.println("accessKey:" + accessKey);
        System.out.println("secretKey:" + secretKey);
    }
}
