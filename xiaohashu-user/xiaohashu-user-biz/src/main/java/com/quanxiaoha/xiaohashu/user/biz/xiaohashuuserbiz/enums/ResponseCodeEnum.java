package com.quanxiaoha.xiaohashu.user.biz.xiaohashuuserbiz.enums;

import com.quanxiaoha.framework.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum ResponseCodeEnum implements BaseExceptionInterface {
    //通用状态异常码
    SYSTEM_ERROR("OSS—10000","出错了，后台努力修复"),
    PARAM_NOT_VALID("OSS-10001","参数错误");

    private final String errorCode;
    private final String errorMessage;
}
