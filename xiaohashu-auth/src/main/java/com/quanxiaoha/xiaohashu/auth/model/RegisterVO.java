package com.quanxiaoha.xiaohashu.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterVO {
    private String phone;
    private String password;
    private String nickname;
    private String verificationCode;
    private Integer registerType;
}
