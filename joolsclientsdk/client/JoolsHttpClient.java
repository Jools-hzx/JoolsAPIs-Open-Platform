package com.jools.joolsclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
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

    public void testGetRequest() {
        //测试 GET
        String res1 = HttpUtil.get("http://localhost:10000/api/name/getName");
        System.out.println("GET 的请求结果为:" + res1);
    }

    public void testPostRequest(@RequestParam String username) {
        //测试 POST
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        String postRes = HttpUtil.post("http://localhost:10000/api/name/getName", params);
        System.out.println("POST 的请求结果为:" + postRes);
    }

    public void testModelPost(@RequestBody User user) {

        //测试 POST user
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest
                .post("http://localhost:10000/api/name/user")
                .addHeaders(getParamsMap(json))     //基于传入的 user 构建消息体
                .body(json)
                .execute();

        //获取结果状态
        int status = httpResponse.getStatus();
        System.out.println("ResponseStatus - " + status);

        //获取结果
        String result = httpResponse.body();
        System.out.println(result);
    }
}
