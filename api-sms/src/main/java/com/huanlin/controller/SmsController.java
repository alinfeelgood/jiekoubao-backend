package com.huanlin.controller;

import com.huanlin.service.SmsService;
import common.BaseResponse;
import common.ErrorCode;
import common.ResultUtils;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@CrossOrigin
public class SmsController {
  @Autowired
  private SmsService smsService;

  //发送短信的方法
    @GetMapping("/send/{mobile}")
    public BaseResponse<Boolean> sendSms(@PathVariable String mobile){

        //调用service发送短信的方法
       boolean isSend  = smsService.send(mobile);
       if(!isSend){
           return ResultUtils.error(ErrorCode.OPERATION_ERROR);
       }
       return ResultUtils.success(isSend);
    }
}
