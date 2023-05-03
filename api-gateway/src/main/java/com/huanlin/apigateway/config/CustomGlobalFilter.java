package com.huanlin.apigateway.config;

//import com.huanlin.apigateway.feign.ApiInterfaceInfoFeignClient;
import com.hunalin.model.entity.InterfaceInfo;
import com.hunalin.model.entity.User;
import com.hunalin.service.InnerInterfaceInfoService;
import com.hunalin.service.InnerUserInterfaceInfoService;
import com.hunalin.service.InnerUserService;
import com.huanlin.apiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
// 全局过滤器
**/
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;
    @DubboReference
    private InnerUserService userService;
    //TODO 由于这里 所有的接口都在同一个 host里 所以先用一个固定接口
    // 如果有多个接口了 则需要客户端 访问不同的接口 存储对应接口所在的host信息放请求头中
    // 网关再根据不同的host路由到对应的接口模块中去 数据库把url拆解成host和url
    public static final String INTERFACEINFO_HOST = "http://localhost:10002";

    public static final List<String>IP_WHILE_LIST = Arrays.asList("127.0.0.1");
    public static final Integer FIVE_MINUTE = 300;
    // 1、用户发送请求到网关
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 2、请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACEINFO_HOST+request.getPath().toString();
        String method = request.getMethod().toString();
        log.info("请求标识:" + request.getId());
        log.info("请求路径:" + path);
        log.info("请求网址:" + request.getURI());
        log.info("请求方法:" + method);
        log.info("请求参数:" + request.getQueryParams());
        String soureceAddress = request.getLocalAddress().getHostName();
        log.info("请求来源地址端口:" + soureceAddress);
        log.info("请求来源地址:" + request.getRemoteAddress());
        //获取response用于黑白名单响应
        ServerHttpResponse response = exchange.getResponse();
        // 3、黑白名单验证
        if(!IP_WHILE_LIST.contains(soureceAddress)){
            return authRequest(response);
        }
        HttpHeaders headers = request.getHeaders();
//        // 4、统一鉴权（aeecssKey secretKey)
        //TODO  这里的权限校验代码 抽取成一个公共类的方法 来进行复用
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        //实际情况 去 数据库 查询 accessKey 和 secretKey
        User invokeUser = null;
        try {
            invokeUser  = userService.getInvokeUser(accessKey);
        }catch (Exception e){
            log.error("rpcGetUserError:"+e);
        }
        if(invokeUser == null){
           return authRequest(response);
        }
//
//        if(!accessKey.equals("200618b62e0eb1097a978d19176416bc")){
//            authRequest(response);
//        }
        if(Long.parseLong(nonce) > 1000000){
            return  authRequest(response);
        }
        long currentTimeMillis = System.currentTimeMillis()/1000;
        //时间戳 不能 超过 5分钟

        if((currentTimeMillis - Long.parseLong(timestamp)) > FIVE_MINUTE ){
            throw new  RuntimeException("请求繁忙，请稍后再请求");
        }

        //用同样的密钥规则 生成密钥 进行比对
        String secretFromBackend = SignUtils.getSign(body, invokeUser.getSecretKey());
        if(sign==null || !sign.equals(secretFromBackend)){
            throw  new RuntimeException("密钥错误");
//            return  authRequest(response);
        }
        // 5、调用接口是否存在？
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path,method);
            if(interfaceInfo == null){
                throw  new  RuntimeException("请求接口不存在");
            }
        }catch (Exception e){
            log.error("rpcGetInterfaceInfoError:"+e);
        }
        Long interfaceInfoId = interfaceInfo.getId();
        Long invokeUserId = invokeUser.getId();
        //是否有调用次数？
        int leftCount = innerUserInterfaceInfoService.getInterfaceLeftCount(interfaceInfoId,invokeUserId);
        if(leftCount <= 0){
            return authRequest(response);
        }
        // 6、调用接口  chain 责任链 这层过滤完成 放行  转到下一层过滤 即路由到配置文件中的地址
//        Mono<Void> filter = chain.filter(exchange);  不采用 原因: 先执行完全局异常 再执行自己的业务
//        log.info("响应:"+response.getStatusCode());
        // 7、响应日志
       return handleGatewayResponse(exchange,chain,interfaceInfo.getId(),invokeUser.getId());
    }

    public Mono<Void> handleGatewayResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceInfoId,long userId){
        try {
            //获得从经过网关的请求
            ServerHttpResponse originalResponse = exchange.getResponse();
            //获取请求响应的数据工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            /**
             * 在进入if之前
             * 先请求转发到对应地址 处理完逻辑后
             * 再跳转到这里进行封装原响应
             */
            if(statusCode == HttpStatus.OK){
                //装饰原响应
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        //如果请求体也是响应式请求 则按下列if中逻辑重写body
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        //8  TODO 调用成功 接口次数+1
                                        try {
                                            //实际公司 有 告警系统
                                            boolean isSuccess = innerUserInterfaceInfoService.addInterfaceCount(interfaceInfoId, userId);
                                            if(isSuccess == false){
                                                log.error("接口次数修改有误");
                                            }
                                        } catch (Exception e) {
                                           log.error("增加接口次数有异常:"+e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                // 8、接口调用次数+1
                                sb2.append(data);
                                log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);

                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 9、调用失败返回统一失败错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                                 invokeErrorRequest(originalResponse);
                        }
                        //如果不是响应式请求，而是真是请求 则直接重新写入body并返回
                        return super.writeWith(body);
                    }
                };
                //构建装饰的响应
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            //降级处理返回数据 即调用原来的处理逻辑 先执行完过滤再调用要路由的方法
            return chain.filter(exchange);
        }catch (Exception e){
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    private Mono<Void> authRequest(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
    private Mono<Void> invokeErrorRequest(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}