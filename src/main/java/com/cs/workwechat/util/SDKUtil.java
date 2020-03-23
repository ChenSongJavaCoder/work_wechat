package com.cs.workwechat.util;

import com.tencent.wework.Finance;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Author: CS
 * @Date: 2020/3/18 11:31 上午
 * @Description:
 */
@Slf4j
public class SDKUtil {

    private static String SECRET =
            "23_HzEpTWCF6y2dxQtIGukH-pRcHFnb3r54ME3Rl8m0";
    private static String CORP_ID =
            "ww2cfbd257dae39722";

    private static long DEFAULT_TIMEOUT = 5000;
    private static long DEFAULT_LIMIT = 500;
    private static long DEFAULT_SEQ = 1;
    private static int SUCCESS = 0;
    private static long SDK;
    private static long SLICE;


    public static String getCorpId() {
        return CORP_ID;
    }

    /**
     * 获取sdk
     *
     * @return
     */
    public static long getSDK() {
        if (SDK > 0) {
            return SDK;
        }
        SDK = Finance.NewSdk();
        log.info("初始化sdk：{}", SDK);
        return SDK;
    }

    /**
     * 销毁sdk
     */
    public static void destroySdk() {
        if (SDK > 0) {
            log.info("销毁sdk：{}", SDK);
            Finance.DestroySdk(SDK);
            SDK = 0L;
        }
    }

    /**
     * 初始化函数
     */
    public static int init() {
        return Finance.Init(getSDK(), CORP_ID, SECRET);
    }

    public static boolean isSuccess(int code) {
        return code == SUCCESS;
    }

    /**
     * 获取加密的聊天数据
     *
     * @return
     */
    public static String getEncryptChatData(Long seq) {
        long slice = Finance.NewSlice();
        int code = Finance.GetChatData(SDK, seq, DEFAULT_LIMIT, null, null, DEFAULT_TIMEOUT, slice);
        if (!isSuccess(code)) {
            log.error("获取聊天数据失败，状态码：{}", code);
            return null;
        }
        String encryptChatData = Finance.GetContentFromSlice(slice);
        Finance.FreeSlice(slice);
        return encryptChatData;
    }

    /**
     * 获取媒体类型文件
     *
     * @param sdkField
     * @param suffix
     */
    public static File getMediaData(String sdkField, String suffix) {
        String indexbuf = "";
        String fileName = "/work/media/" + System.currentTimeMillis() + suffix;
        while (true) {
            long mediaData = Finance.NewMediaData();
            int code = Finance.GetMediaData(getSDK(), indexbuf, sdkField, null, null, DEFAULT_TIMEOUT, mediaData);
            if (!isSuccess(code)) {
                log.error("获取媒体类型文件错误，状态码：{}", code);
                return null;
            }
            log.info("getmediadata outindex len:{}, data_len:{}, is_finis:{}", Finance.GetIndexLen(mediaData), Finance.GetDataLen(mediaData), Finance.IsMediaDataFinish(mediaData));
            try {
                FileOutputStream outputStream = new FileOutputStream(new File(fileName));
                outputStream.write(Finance.GetData(mediaData));
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Finance.IsMediaDataFinish(mediaData) == 1) {
                Finance.FreeMediaData(mediaData);
                File file = new File(fileName);
                return file;
            } else {
                indexbuf = Finance.GetOutIndexBuf(mediaData);
                Finance.FreeMediaData(mediaData);
            }
        }

    }
}
