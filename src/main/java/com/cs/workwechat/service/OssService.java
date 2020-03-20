package com.cs.workwechat.service;

import com.cs.workwechat.pojo.enums.MediaType;

import java.io.File;


/**
 * @Author: CS
 * @Date: 2020/3/19 5:39 下午
 * @Description:
 */
public interface OssService {

    /**
     * 上传文件到存储空间
     *
     * @param tempFile 文件byte数组
     * @return 文件唯一标识
     */
    String upload(File tempFile, MediaType mediaType);


}
