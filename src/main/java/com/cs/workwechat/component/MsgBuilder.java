package com.cs.workwechat.component;

import com.cs.workwechat.entity.BaseMsg;
import com.cs.workwechat.pojo.*;
import com.cs.workwechat.pojo.enums.MediaType;
import com.cs.workwechat.pojo.enums.MsgType;
import com.cs.workwechat.service.OssService;
import com.cs.workwechat.util.SDKUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * @Author: CS
 * @Date: 2020/3/19 2:44 下午
 * @Description:
 */
@Slf4j
@Component
public class MsgBuilder {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OssService ossService;


    public BaseMsg buildBaseMsg(MsgType msgType, String chatData, Long seq) throws Exception {
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
                uploadOss(imageMsg.getImage().getSdkfileid(), MediaType.image, imageMsg);
                return imageMsg;
            case emotion:
                EmotionMsg emotionMsg = objectMapper.readValue(chatData, EmotionMsg.class);
                emotionMsg.setContent(emotionMsg.getEmotion());
                emotionMsg.setSeq(seq);
                uploadOss(emotionMsg.getEmotion().getSdkfileid(), MediaType.emotion, emotionMsg);
                return emotionMsg;
            case file:
                FileMsg fileMsg = objectMapper.readValue(chatData, FileMsg.class);
                fileMsg.setContent(fileMsg.getFile());
                fileMsg.setSeq(seq);
                uploadOss(fileMsg.getFile().getSdkfileid(), MediaType.file, fileMsg);
                return fileMsg;
            case video:
                VideoMsg videoMsg = objectMapper.readValue(chatData, VideoMsg.class);
                videoMsg.setContent(videoMsg.getVideo());
                videoMsg.setSeq(seq);
                uploadOss(videoMsg.getVideo().getSdkfileid(), MediaType.video, videoMsg);
                return videoMsg;
            case voice:
                VoiceMsg voiceMsg = objectMapper.readValue(chatData, VoiceMsg.class);
                voiceMsg.setContent(voiceMsg.getVoice());
                voiceMsg.setSeq(seq);
                uploadOss(voiceMsg.getVoice().getSdkfileid(), MediaType.voice, voiceMsg);
                return voiceMsg;
            case weapp:
                WeappMsg weappMsg = objectMapper.readValue(chatData, WeappMsg.class);
                weappMsg.setContent(weappMsg.getWeapp());
                weappMsg.setSeq(seq);
                return weappMsg;
            case revoke:
                RevokeMsg revokeMsg = objectMapper.readValue(chatData, RevokeMsg.class);
                revokeMsg.setContent(revokeMsg.getRevoke());
                revokeMsg.setSeq(seq);
                return revokeMsg;
            case card:
                break;
            case link:
                LinkMsg linkMsg = objectMapper.readValue(chatData, LinkMsg.class);
                linkMsg.setContent(linkMsg.getLink());
                linkMsg.setSeq(seq);
                return linkMsg;
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
            case docmsg:
                break;
            case collect:
                break;
            case meeting:
                break;
            case calendar:
                break;
            case location:
                break;
            case markdown:
                break;
            case redpacket:
                break;
            case chatrecord:
                break;
            default:
                log.error("未知数据类型！");
        }

        return null;
    }


    /**
     * 上传至oss
     *
     * @param sdkField
     * @param mediaType
     * @param baseMsg
     */
    private void uploadOss(String sdkField, MediaType mediaType, BaseMsg baseMsg) {
        String suffix = mediaType.getSuffix();
        if (mediaType.equals(MediaType.file)) {
            FileMsg fileMsg = (FileMsg) baseMsg;
            suffix = "." + fileMsg.getFile().getFileext();
        }
        File file = SDKUtil.getMediaData(sdkField, suffix);
        if (Objects.isNull(file)) {
            return;
        }
        String path = ossService.upload(file, mediaType);
        baseMsg.setOssPath(path);
        file.delete();
    }
}
