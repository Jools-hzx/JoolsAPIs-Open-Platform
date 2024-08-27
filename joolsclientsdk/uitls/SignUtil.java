package com.jools.joolsclientsdk.uitls;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/8/20 22:25
 * @description: 生成 API 签名的工具类
 */
public class SignUtil {

    public static final String TEST_ACCESS_KEY = "jools";

    public static final String TEST_SECRET_KEY = "123456";


    /**
     * @param body      请求参数体
     * @param secretKey 密钥
     * @return 基于参数体和密钥拼接之后生成的签名字符串
     */
    public static String getSign(String body, String secretKey) {
        //使用 SHA256 算法的 Digester
        Digester sha = new Digester(DigestAlgorithm.SHA256);
        //构建签名内容，将哈希映射为字符串并且拼接密钥
        String content = body + "." + secretKey;
        return sha.digestHex(content);
    }
}
