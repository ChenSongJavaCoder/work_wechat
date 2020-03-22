package com.cs.workwechat.mapper;

import com.cs.workwechat.entity.Employee;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface EmployeeMapper extends Mapper<Employee>, MySqlMapper<Employee> {
}
