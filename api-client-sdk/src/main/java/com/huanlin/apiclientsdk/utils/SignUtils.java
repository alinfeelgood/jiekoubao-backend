package com.huanlin.apiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignUtils {
    /**
     * 生成签名
     * @param body
     * @param secretKey
     * @return
     */
    public static String getSign(String body, String secretKey){
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        //用户请求内容 拼接 密钥 进行加密
        String content = body + "." + secretKey;
        String digest = md5.digestHex(content);
        return  digest;
    }
}
