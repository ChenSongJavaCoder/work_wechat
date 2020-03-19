package com.cs.workwechat.component;

import com.cs.workwechat.pojo.*;
import com.cs.workwechat.pojo.enums.MediaType;
import com.cs.workwechat.pojo.enums.MsgType;
import com.cs.workwechat.util.SDKUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: CS
 * @Date: 2020/3/19 2:44 下午
 * @Description: TODO 媒体类文件需要上传oss
 */
@Slf4j
@Component
public class MsgBuilder {

    @Autowired
    ObjectMapper objectMapper;

    public BaseMsg buildBaseMsg(MsgType msgType, String chatData, Long seq) throws JsonProcessingException {
        switch (msgType) {
            case text:
                TextMsg textMsg = objectMapper.readValue(chatData, TextMsg.class);
                textMsg.setContent(textMsg.getText());
                textMsg.setSeq(seq);
                return textMsg;
            case image:
                ImageMsg imageMsg = objectMapper.readValue(chatData, ImageMsg.class);
                imageMsg.setContent(imageMsg.getImage());
                imageMsg.setSeq(seq);
                SDKUtil.getMediaData(imageMsg.getImage().getSdkfileid(), MediaType.image.getSuffix());
                return imageMsg;
            case weapp:
                WeappMsg weappMsg = objectMapper.readValue(chatData,WeappMsg.class);
                weappMsg.setContent(weappMsg.getWeapp());
                weappMsg.setSeq(seq);
                return weappMsg;
            case card:
                break;
            case file:
                break;
            case link:
                break;
            case news:
                break;
            case todo:
                break;
            case vote:
                break;
            case agree:
                break;
            case mixed:
                break;
            case video:
                VideoMsg videoMsg = objectMapper.readValue(chatData,VideoMsg.class);
                videoMsg.setContent(videoMsg.getVideo());
                videoMsg.setSeq(seq);
                SDKUtil.getMediaData(videoMsg.getVideo().getSdkfileid(), MediaType.video.getSuffix());
                return videoMsg;
            case voice:
                VoiceMsg voiceMsg = objectMapper.readValue(chatData,VoiceMsg.class);
                voiceMsg.setContent(voiceMsg.getVoice());
                voiceMsg.setSeq(seq);
                SDKUtil.getMediaData(voiceMsg.getVoice().getSdkfileid(), MediaType.voice.getSuffix());
                return voiceMsg;
            case docmsg:break;
            case revoke:
                RevokeMsg revokeMsg = objectMapper.readValue(chatData,RevokeMsg.class);
                revokeMsg.setContent(revokeMsg.getRevoke());
                revokeMsg.setSeq(seq);
                return revokeMsg;
            case collect:break;
            case emotion:break;
            case meeting:break;
            case calendar:break;
            case location:break;
            case markdown:break;
            case redpacket:break;
            case chatrecord:break;
            default:
                log.error("未知数据类型！");
        }

        return null;
    }
}