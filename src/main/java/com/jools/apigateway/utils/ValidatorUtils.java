package com.jools.apigateway.utils;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/9/6 10:29
 * @description: TODO
 */
public class ValidatorUtils {

    //判断请求是否在 3 分钟以内
    public static boolean isWithinThreeMinutes(String timeStamp) {

        try {
            long receivedTimeMillis = Long.parseLong(timeStamp);

            //注意 客户端传入的时间戳是按照 毫秒 -> 秒存储的，此处需要同步 / 1000
            long currentTimeMillis = System.currentTimeMillis() / 1000; //除以 1000 获取秒数

            //计算时间差
            // 计算时间差
            Duration duration = Duration.between(
                    Instant.ofEpochMilli(receivedTimeMillis),
                    Instant.ofEpochMilli(currentTimeMillis)
            );
            return duration.toMinutes() <= 3;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
