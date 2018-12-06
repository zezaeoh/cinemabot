package com.jaegeon.cinemabot.web;

import com.jaegeon.cinemabot.service.MessageProcessingService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;


@RestController
public class WebRestController {

    @GetMapping("/hello")
    public String hello() {
        return "HelloWorld";
    }

    @GetMapping("/keyboard")
    public String keyboard(){

        JSONObject jobj = new JSONObject();
        jobj.put("type", "text");

        return jobj.toJSONString();
    }

    @PostMapping(value = "/message", headers = "Accept=application/json")
    public String message(@RequestBody JSONObject resObj){
        String msg;
        msg = (String) resObj.get("content");
        JSONObject jObjRes = new JSONObject();
        JSONObject jObjText =  new JSONObject();

        String reMsg = MessageProcessingService.processMssage(msg);
        if(reMsg == null || reMsg.isEmpty())
            jObjText.put("text", "이해하지 못하는 명령어 입니다!");
        else
            jObjText.put("text", reMsg);

        jObjRes.put("message", jObjText);
        return jObjRes.toJSONString();
    }

}