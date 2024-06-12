package com.practicum.neuron.entity.account;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserInfo {
    // 用户名
    private String username;

    // 真实姓名
    private String name;

    // 身份证号
    private String id_number;

    // 出生日期
    private LocalDate birthday;

    //性别
    private String gender;

    // 住址
    private String address;

    // 手机号
    private String phone;

    // 邮箱
    private String email;

    // 学历
    private String education_level;

    // 毕业院校
    private String graduation_institution;

    // 毕业专业
    private String graduation_major;

    // 入职时间
    private LocalDate hire_date;

    // 就职部门
    private String department;

    // 职位
    private String position;
}
