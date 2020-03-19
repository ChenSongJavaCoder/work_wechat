package com.cs.workwechat.controller;

import com.cs.workwechat.mapper.WorkWechatMsgMapper;
import com.cs.workwechat.pojo.BaseMsg;
import com.cs.workwechat.pojo.TextMsg;
import com.cs.workwechat.pojo.enums.MsgType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: CS
 * @Date: 2020/3/19 3:03 下午
 * @Description:
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    WorkWechatMsgMapper workWechatMsgMapper;

    @Autowired
    ObjectMapper objectMapper;

//    @Scheduled(fixedRate = 600 * 1000)
    @GetMapping("/test")
    public String test() throws JsonProcessingException {

        BaseMsg o = workWechatMsgMapper.selectByPrimaryKey(2);
        log.info(objectMapper.writeValueAsString(o));
        try {
            if (o.getMsgtype().equals(MsgType.text)) {
                TextMsg.Text text = objectMapper.readValue((String) o.getContent(), TextMsg.Text.class);
                o.setContent(text);
            }
            String s = objectMapper.writeValueAsString(o);
            log.info(s);
            return s;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "错了呀";
    }

}
