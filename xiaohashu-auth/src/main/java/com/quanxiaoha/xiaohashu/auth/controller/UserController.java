package com.quanxiaoha.xiaohashu.auth.controller;

import com.quanxiaoha.framework.biz.operationlog.aspect.ApiOperationLog;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.auth.model.RegisterVO;
import com.quanxiaoha.xiaohashu.auth.model.UserVO;
import com.quanxiaoha.xiaohashu.auth.model.user.UpdatePasswordVO;
import com.quanxiaoha.xiaohashu.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Response<String> login(@RequestBody UserVO userVO) {
        userService.login(userVO);
        return userService.login(userVO);
    }
    @PostMapping("/register")
    public Response<String> register(@RequestBody RegisterVO registerVO) {
        return userService.register(registerVO);
    }

    @PostMapping("/logout")
    @ApiOperationLog(description = "账号登出")
    public Response<?> logout(@RequestHeader("userId") String userId) {
        //todo 账号登录逻辑待实现
        log.info("==> 网关透传过来的用户ID:{}",userId);
        Integer userIds = Integer.valueOf (userId);
        return userService.logout();
    }

    @PostMapping("/password/update")
    @ApiOperationLog(description = "修改密码")
    public Response<?> updatePassword(@RequestBody UpdatePasswordVO updatePasswordVO) {
        return userService.updatePassword(updatePasswordVO);
    }
}
