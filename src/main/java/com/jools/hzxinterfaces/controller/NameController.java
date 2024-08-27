package com.jools.hzxinterfaces.controller;

import com.jools.joolsclientsdk.model.User;
import com.jools.joolsclientsdk.uitls.SignUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/20 17:57
 * @description: TODO
 */

@RequestMapping("/name")
@RestController
public class NameController {

    @GetMapping("/getName")
    @ResponseBody
    public String getNameByGet() {
        return "GET-请求名称:" + " Jools ";
    }

    @PostMapping("/getName")
    @ResponseBody
    public String getNameByPost(@RequestParam("username") String name) {
        return "POST-请求名称:" + name;
    }

    @PostMapping("/user")
    @ResponseBody
    public String getNameByUser(@RequestBody User user, HttpServletRequest request) {

        System.out.println("进入了 getNameByUser(@RequestBody User user, HttpServletRequest request) 接口");

        //基于请求头获取参数
        //accessKey 可以先到数据库去校验
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String body = request.getHeader("body");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");

        //校验权限，这里模拟以下，直接判断与测试 accessKey 是否一致
        if (!accessKey.equals(SignUtil.TEST_ACCESS_KEY)) {
            throw new RuntimeException("无权限! - AccessKey 出错");
        }

        //校验随机数
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限 - 随机数字出错");
        }

        //校验时间戳如果时间差距大于 3 分钟报错
        if (!isWithinThreeMinutes(timestamp)) {
            throw new RuntimeException("无权限-时间戳出错");
        }

        //判断签名
        //如何拼接这个 sign? 就按照客户端拼接的方式来进行。
        //secretKey 可以后期通过查询数据库获取
        if (!sign.equals(SignUtil.getSign(body, SignUtil.TEST_SECRET_KEY))) {
            throw new RuntimeException("无权限 - API签名校验出错");
        }

        return "POST 用户名称是:" + user.getName();
    }


    private boolean isWithinThreeMinutes(String timeStamp) {

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
