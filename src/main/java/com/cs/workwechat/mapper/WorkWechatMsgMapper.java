package com.cs.workwechat.mapper;

import com.cs.workwechat.pojo.BaseMsg;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;

/**
 * @Author: CS
 * @Date: 2020/3/12 5:13 下午
 * @Description:
 */
@Repository
public interface WorkWechatMsgMapper extends BaseMapper<BaseMsg>, SelectByExampleMapper<BaseMsg>, MySqlMapper<BaseMsg> {
}
