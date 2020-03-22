package com.cs.workwechat.mapper;

import com.cs.workwechat.entity.Customer;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface CustomerMapper extends Mapper<Customer>, MySqlMapper<Customer> {
}
