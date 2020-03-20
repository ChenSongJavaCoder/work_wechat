package com.cs.workwechat.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.cs.workwechat.config.OssConfig;
import com.cs.workwechat.pojo.enums.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @Author: CS
 * @Date: 2020/3/19 5:39 下午
 * @Description:
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    OssConfig ossConfig;

    @Override
    public String upload(File tempFile, MediaType mediaType) {
        String key = ossConfig.getFilePath(mediaType) + tempFile.getName();
        log.info("上传key = {}", key);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(mediaType.getContentType());
        objectMetadata.addUserMetadata("filename", key);

        OSSClient ossClient = ossConfig.ossClient();
        ossClient.putObject(ossConfig.getBucketName(), key, tempFile, objectMetadata);
        ossClient.setObjectAcl(ossConfig.getBucketName(), key, CannedAccessControlList.PublicRead);
        ossClient.shutdown();
        log.info("上传key = {} 的图片,oss文件地址 = {}", key, ossConfig.getUrlPrefix() + "/" + key);

        return key;
    }


}
