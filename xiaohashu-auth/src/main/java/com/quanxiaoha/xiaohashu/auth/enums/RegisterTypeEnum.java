package com.quanxiaoha.xiaohashu.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegisterTypeEnum {
    PASSWORD(1,"密码注册"),
    SMS(2,"短信验证码注册");

    private final int code;
    private final String desc;
}
