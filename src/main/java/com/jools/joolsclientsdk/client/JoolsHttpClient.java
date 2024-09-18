package com.jools.joolsclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jools.joolsclientsdk.model.User;
import com.jools.joolsclientsdk.uitls.SignUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/20 19:59
 * @description: TODO
 */
public class JoolsHttpClient {

    private String accessKey;
    private String secretKey;

    //请求网关URI
    private static final String GATEWAY_URI = "http://localhost:8899";

    public JoolsHttpClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public Map<String, String> getParamsMap(String body) {
        Map<String, String> params = new HashMap<>();
        params.put("accessKey", this.accessKey);
//        params.put("secretKey", this.secretKey);

        //生成长度为 4 的随机数
        params.put("nonce", RandomUtil.randomNumbers(4));

        //配置消息体
        params.put("body", body);

        //配置当前的时间戳
        //// System.currentTimeMillis()返回当前时间的毫秒数。通过除以1000，可以将毫秒数转换为秒数，以得到当前时间戳的秒级表示
        //	// String.valueOf()方法用于将数值转换为字符串。在这里，将计算得到的时间戳（以秒为单位）转换为字符串
        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        //配置生成的密钥
        params.put("sign", SignUtil.getSign(body, this.secretKey));
        return params;
    }

    public String testGetRequest() {
        //测试 GET
        String res1 = HttpUtil.get(GATEWAY_URI + "/api/name/getName");
        System.out.println("GET 的请求结果为:" + res1);
        return res1;
    }

    public String testPostRequest(@RequestParam String username) {
        //测试 POST
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        String postRes = HttpUtil.post(GATEWAY_URI + "/api/name/getName", params);
        System.out.println("POST 的请求结果为:" + postRes);
        return postRes;
    }

    public String testModelPost(@RequestBody User user) {

        //测试 POST user
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest
                .post(GATEWAY_URI + "/api/name/user")
                .addHeaders(getParamsMap(json))     //Json格式的 User 信息构建请求头参数
                .body(json)
                .execute();

        //获取结果状态
        int status = httpResponse.getStatus();
        System.out.println("ResponseStatus - " + status);

        // 处理限流 (HTTP 429) 和 处理降级 (HTTP 503)
        /*
        ResponseStatus - 429
        限流响应: Blocked by Sentinel: DegradeException
         */
        if (status != HttpStatus.HTTP_OK) {
            // 打印并返回限流提示信息
            String result = httpResponse.body();
            System.out.println("流控响应: " + result);
            if (result.contains("DegradeException")) return "服务开小差了，请稍后再试";
            else if (result.contains("FlowException")) return "请求过于频繁，请稍后再试";
            return "服务异常，请稍后再试";
        }

        //获取结果
        String result = httpResponse.body();
        System.out.println(result);

        return result;
    }
}
