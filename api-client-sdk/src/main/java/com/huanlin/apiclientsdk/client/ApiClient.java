package com.huanlin.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.huanlin.apiclientsdk.model.LoveWords;
import com.huanlin.apiclientsdk.model.User;
import com.huanlin.apiclientsdk.model.WeatherCitys;

import java.util.HashMap;
import java.util.Map;

import static com.huanlin.apiclientsdk.utils.SignUtils.getSign;


/**
 * 调用第三方接口客户端
 */
public class ApiClient {

    private  String accessKey;

    private  String secretKey;

    public static final String Gateway_Port = "http://localhost:10003";

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name){
        //可以单独传入http参数 这样参数会自动做URL编码，拼接在URL中
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String res = HttpUtil.get(Gateway_Port+"/api/getname", paramMap);
        System.out.println(res);
        return res;
    }
    public String getNameByPost(String name){
        //可以单独传入http参数 这样参数会自动做URL编码，拼接在URL中
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String res = HttpUtil.post(Gateway_Port+"/api/postname", paramMap);
        System.out.println(res);
        return res;
    }
    private Map<String,String>  addHeader(String body){
        HashMap<String, String> header = new HashMap<>();
        header.put("accessKey",accessKey);
        //不能直接发送给后端
//        header.put("secretKey",secretKey);
        //随机码 防止重放攻击
        header.put("nonce", RandomUtil.randomNumbers(5));
        header.put("body",body);
        //时间戳 防止重新发送年龄过于久远的请求
        header.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        //签名认证
        header.put("sign",getSign(body,secretKey));
        return  header;
    }


    public String getUserByPost( User user){
        String json = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post(Gateway_Port+"/api/user")
                              .body(json)
                              .addHeaders(addHeader(json))
//                              .charset(StandardCharsets.UTF_8)

                              .execute();
        System.out.println(response.getStatus());
        return response.body();
    }

    //随机情话接口
    public String getLoveWordsGet() {
//        String json = JSONUtil.toJsonStr(loveWords);
        HttpResponse httpResponse = HttpRequest.get(Gateway_Port + "/api/lovewords")
                .body("")
                .addHeaders(addHeader(""))
                .execute();
        String result = httpResponse.body();
        return result;
    }

    //获取热点新闻
    public String getHotNewsGet() {
        HttpResponse httpResponse = HttpRequest.get(Gateway_Port + "/api/hotNews")
                .addHeaders(addHeader(""))
                .body("")
                .execute();
        String result = httpResponse.body();
        return result;
    }
    //随机返回抖音美女视频
    public String getdyGirlGet() {
        HttpResponse httpResponse = HttpRequest.get(Gateway_Port + "/api/dygirl")
                .addHeaders(addHeader(""))
                .body("")
                .execute();
        String result = httpResponse.body();
        return result;
    }

    //随机返回爬虫美女视频
    public String getpcGirlGet() {
        HttpResponse httpResponse = HttpRequest.get(Gateway_Port + "/api/pcgirl")
                .addHeaders(addHeader(""))
                .body("")
                .execute();
        String result = httpResponse.body();
        return result;
    }

    //获取微信状态最火歌曲
    public String getWxTopSongGet() {
//        String jsonStr = JSONUtil.toJsonStr(weatherCitys);

        HttpResponse httpResponse = HttpRequest.get(Gateway_Port + "/api/wxtopsongs")
                .addHeaders(addHeader(""))
                .body("")
                .execute();
        String result = httpResponse.body();
        return result;
    }

    //获取中文版chatgpt接口
    public String getChatGPTAPIGet() {
//        String jsonStr = JSONUtil.toJsonStr(weatherCitys);

        HttpResponse httpResponse = HttpRequest.get(Gateway_Port + "/api/chatgptapi")
                .addHeaders(addHeader(""))
                .body("")
                .execute();
        String result = httpResponse.body();
        return result;
    }
    //每日早报
    public String getDailyMorningNewsGet() {
//        String jsonStr = JSONUtil.toJsonStr(weatherCitys);

        HttpResponse httpResponse = HttpRequest.get(Gateway_Port + "/api/dailymorningnews")
                .addHeaders(addHeader(""))
                .body("")
                .execute();
        String result = httpResponse.body();
        return result;
    }
}
