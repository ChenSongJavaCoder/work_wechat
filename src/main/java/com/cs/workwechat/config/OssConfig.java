package com.cs.workwechat.config;

import com.aliyun.oss.OSSClient;
import com.cs.workwechat.pojo.enums.MediaType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: CS
 * @Date: 2020/3/19 5:46 下午
 * @Description:
 */
@Getter
@Component
public class OssConfig {

    @Value("${aliyun.oss.endPoint}")
    String endPoint;

    @Value("${aliyun.oss.accessKeyId}")
    String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    String accessKeySecret;

    @Value("${aliyun.oss.files.bucketName}")
    String bucketName;

    @Value("${aliyun.oss.files.prefix}")
    String prefix;

    @Value("${aliyun.oss.files.image}")
    String image;

    @Value("${aliyun.oss.files.video}")
    String video;

    @Value("${aliyun.oss.files.voice}")
    String voice;

    @Value("${aliyun.oss.urlPrefix}")
    String urlPrefix;

    public OSSClient ossClient() {
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        return ossClient;
    }


    public String getFilePath(MediaType mediaType) {
        String path;
        switch (mediaType) {
            case voice:
                path = prefix + voice;
                break;
            case video:
                path = prefix + video;
                break;
            case image:
                path = prefix + image;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return path;
    }
}
