package com.cs.workwechat.component;

import com.cs.workwechat.mapper.WorkWechatMsgMapper;
import com.cs.workwechat.pojo.*;
import com.cs.workwechat.pojo.enums.MediaType;
import com.cs.workwechat.pojo.enums.MsgType;
import com.cs.workwechat.util.RSAUtils;
import com.cs.workwechat.util.SDKUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.tencent.wework.Finance;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: CS
 * @Date: 2020/3/11 4:56 下午
 * @Description:
 */
@Slf4j
@Component
public class Task {

    @Autowired
    WorkWechatMsgMapper workWechatMsgMapper;

    @Autowired
    MsgBuilder msgBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedRate = 60 * 1000)
    public void pullChatData() {
        try {
            getChatData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getChatData() throws Exception {
        SDKUtil.init();
        PageHelper.startPage(1, 1);
        Example example = new Example(BaseMsg.class);
        example.orderBy("seq").desc();
        List<BaseMsg> latest = workWechatMsgMapper.selectByExample(example);
        Long seq = 1L;
        if (!CollectionUtils.isEmpty(latest)) {
            seq = latest.get(0).getSeq() + 1L;
        }
        String en = SDKUtil.getEncryptChatData(seq);
        if (StringUtils.isEmpty(en)) {
            return null;
        }
        ChatMsg chat = objectMapper.readValue(en, ChatMsg.class);
        List<ChatMsg.ChatdataBean> data = chat.getChatdata();
        int idx = 1;
        List<BaseMsg> baseMsgList = new ArrayList<>(512);
        for (ChatMsg.ChatdataBean bean : data) {
            byte[] encryptKey = RSAUtils.decryptByPrivateKey(Base64.decode(bean.getEncrypt_random_key()), null);
            String key = new String(encryptKey);
            long slice = Finance.NewSlice();
            int code = Finance.DecryptData(SDKUtil.getSDK(), key, bean.getEncrypt_chat_msg(), slice);
            if (!SDKUtil.isSuccess(code)) {
                log.error("第{}条解密聊天数据失败，状态码：{}", idx, code);
            }
            String chatData = Finance.GetContentFromSlice(slice);
            Finance.FreeSlice(slice);

            log.info("第{}条解密后的聊天数据:{}", idx, chatData);
            BaseMsg temp = objectMapper.readValue(chatData, BaseMsg.class);
            MsgType msgType = temp.getMsgtype();
            log.info("该条消息类型为：{}", msgType);
            BaseMsg baseMsg = msgBuilder.buildBaseMsg(msgType, chatData, bean.getSeq());
            baseMsgList.add(baseMsg);
            idx++;
        }
        List list = baseMsgList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(list)) {
            workWechatMsgMapper.insertList(list);
        }

        return null;

    }


}
