package com.quanxiaoha.xiaohashu.auth.model;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO {
    private String phone;
    private String password;
    private String status;
}
