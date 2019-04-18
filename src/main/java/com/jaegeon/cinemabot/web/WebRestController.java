package com.jaegeon.cinemabot.web;

import com.jaegeon.cinemabot.service.MessageProcessingService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
public class WebRestController {

    @CrossOrigin
    @GetMapping("/hello")
    public String hello() {
        return "HelloWorld";
    }

    @CrossOrigin
    @GetMapping("/keyboard")
    public String keyboard(){

        JSONObject jobj = new JSONObject();
        jobj.put("type", "text");

        return jobj.toJSONString();
    }

    @CrossOrigin
    @PostMapping(value = "/message", headers = "Accept=application/json")
    public String message(@RequestBody JSONObject resObj){
        String msg;
        boolean is_kakao = false;
        msg = (String) resObj.get("content");
        if(msg == null || msg.isEmpty()){
            HashMap action = (HashMap) resObj.get("action");
            if(action != null) {
                HashMap params= (HashMap) action.get("params");
                if(params != null) {
                    msg = (String) params.get("content");
                    is_kakao = true;
                }
            }
        }
        JSONObject jObjRes = new JSONObject();
        JSONObject jObjText =  new JSONObject();

        String reMsg = MessageProcessingService.processMssage(msg, is_kakao);
        if(reMsg == null || reMsg.isEmpty())
            jObjText.put("text", "이해하지 못하는 명령어 입니다!");
        else
            jObjText.put("text", reMsg);

        jObjRes.put("message", jObjText);
        return jObjRes.toJSONString();
    }

}