package com.quanxiaoha.xiaohashu.user.biz.enums;

import com.quanxiaoha.framework.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCodeEnum implements BaseExceptionInterface {
    //通用状态异常码
    SYSTEM_ERROR("USER—10000","出错了，后台努力修复"),
    PARAM_NOT_VALID("USER-10001","参数错误");

    private final String errorCode;
    private final String errorMessage;
}
