package com.huanlin.apifrontinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.huanlin.apiclientsdk.model.User;
import com.huanlin.apiclientsdk.model.WeatherCitys;
import com.huanlin.apiclientsdk.utils.SignUtils;
import com.huanlin.apifrontinterface.mapper.LoveWordsMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 名称 API
 */
@RestController
@RequestMapping("/")
public class NameController {
    @Resource
    private LoveWordsMapper loveWordsMapper;
    @GetMapping("/getname")
    public String getNameByGet(String name){

        return  name;
    }
    @PostMapping("/postname")
    public String getNameByPost(@RequestParam  String name){
        return name;
    }
    @PostMapping("/user")
    public String getUserByPost(@RequestBody User user, HttpServletRequest request){
        //TO DO  这里的权限校验代码 抽取成一个公共类的方法 来进行复用
        String accessKey = request.getHeader("accessKey");
        //TO DO openfeign 远程调用userservice接口 查询当前登陆用户 接着查出该用户对应的secretKey
        String nonce = request.getHeader("nonce");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        String timestamp = request.getHeader("timestamp");
        //实际情况 去 数据库 查询 accessKey 和 secretKey
        if(!accessKey.equals("200618b62e0eb1097a978d19176416bc")){
            throw  new  RuntimeException("用户标识错误");
        }
        if(Long.parseLong(nonce) > 1000000){
            throw new  RuntimeException("无权限");
        }
        long currentTimeMillis = System.currentTimeMillis()/1000;
        //时间戳 不能 超过 5分钟
        if(Long.parseLong(timestamp) - currentTimeMillis > 300 ){
            throw new  RuntimeException("请求繁忙，请稍后再请求");
        }

        //用同样的密钥规则 生成密钥 进行比对  TO DO 数据库查询
        String secretFromBackend = SignUtils.getSign(body, "cc032856e38ebdbbb6ad01138f946517");
        if(!sign.equals(secretFromBackend)){
            throw  new RuntimeException("密钥错误");
        }
        //调用成功 次数+1
            return  user.getUsername();
    }
//    @RequestBody LoveWords loveWords
    @GetMapping("/lovewords")
    public String getLoveWords(){
        //to do 根据type返回不同格式的数据
        String loveWord = loveWordsMapper.getRandomLoveWords();
        return loveWord;
    }

    @GetMapping("/dygirl")
    public String getdyGirlGet(){
        Map<String,String> map= new HashMap<>();
        map.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44");
        HttpResponse httpResponse = HttpRequest.get("https://tucdn.wpon.cn/api-girl/index.php")
                .addHeaders(map)
                .execute();
//        String location = httpResponse.header("Location");
//        HttpResponse httpResponse1 = HttpRequest.get(location).execute();
        return httpResponse.body();
    }

    @GetMapping("/pcgirl")
    public String getpcGirlGet(){
        Map<String,String> map= new HashMap<>();
        map.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44");
        HttpResponse httpResponse = HttpRequest.get("https://tucdn.wpon.cn/api-girl/index.php")
                .addHeaders(map)
                .execute();
        return httpResponse.body();
    }
    @GetMapping("/hotNews")
    public String getHotNewsGet(){
        Map<String,String> map= new HashMap<>();
        map.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44");
        HttpResponse httpResponse = HttpRequest.get("https://v.api.aa1.cn/api/topbaidu/")
                .addHeaders(map)
                .execute();
        return httpResponse.body();
    }

    @GetMapping("/wxtopsongs")
    public String getWxTopSongGet(){
        Map<String,String> map= new HashMap<>();
        map.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44");
        HttpResponse httpResponse = HttpRequest.get("https://api.wer.plus/api/wxtop")
                .addHeaders(map)
                .execute();
        return httpResponse.body();
    }

    @GetMapping("/chatgptapi")
    public String getChatGPTAPIGet(){
//        Map<String,String> map= new HashMap<>();
//        map.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44");
//        HttpResponse httpResponse = HttpRequest.get("https://xixibot.com")
//                .addHeaders(map)
//                .execute();
        String url = "https://xixibot.com";
        return url;
    }

    @GetMapping("/dailymorningnews")
    public String getDailyMorningNewsGet(){
        Map<String,String> map= new HashMap<>();
        map.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44");
        HttpResponse httpResponse = HttpRequest.get("https://hub.onmicrosoft.cn/public/news")
                .addHeaders(map)
                .execute();
        return httpResponse.body();
    }
}
