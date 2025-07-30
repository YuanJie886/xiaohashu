package com.quanxiaoha.xiaohashu.auth.service;

import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.auth.model.RegisterVO;
import com.quanxiaoha.xiaohashu.auth.model.UserVO;
import com.quanxiaoha.xiaohashu.auth.model.user.UpdatePasswordVO;

public interface UserService {

    Response<String> login(UserVO userVO);

    Response<?> logout();

    Response<String> register(RegisterVO registerVO);

    Response<Boolean> sendVerificationCode(String phone);

    Response<?> updatePassword(UpdatePasswordVO updatePasswordVO);
}
