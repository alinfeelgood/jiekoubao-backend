package com.huanlin.service.impl;

import com.huanlin.service.SmsService;
import com.huanlin.utils.HttpUtils;
import com.huanlin.utils.RandomUtil;
import com.huanlin.utils.constant.RedisConstant;
import common.ErrorCode;
import exception.BusinessException;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //发送短信的方法
    @Override
    public boolean send( String phone) {
        //1、从redis中获取验证码 有效期五分钟
        String codeFromRedis = redisTemplate.opsForValue().get(RedisConstant.SMS_CODE_KEY+phone);


        //2、如果redis没有 则生成code 用阿里云发送
       if(!StringUtils.isEmpty(codeFromRedis)){
           long valueTime = Long.parseLong(codeFromRedis.split("_")[1]);
           if(System.currentTimeMillis()-valueTime<60000){
               //60秒内不能再发
               throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"请60秒后再重新发送验证码");
           }
//           return true;
       }
        //生成随机值 传递阿里云进行发送
        String code = RandomUtil.getFourBitRandom();
        String host = "https://dfsmsv2.market.alicloudapi.com";
        String path = "/data/send_sms_v2";
        String method = "GET";
        String appcode = "4cbbf48b00574b6483bc65cbb9796639";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        /**
         * 传参格式要正确 不然会报400请求错误!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * 也可将code封装为map 再用fastjson的JsonObject。toJSONString
         * 转化为 code：1234 即json格式
         */
        bodys.put("content",  "code:"+code);
        bodys.put("phone_number", phone);
        bodys.put("template_id", "TPL_0000");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().set(RedisConstant.SMS_CODE_KEY+phone,code+"_"+ System.currentTimeMillis(),5,TimeUnit.MINUTES);
        return true;
    }
}
