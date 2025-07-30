package com.quanxiaoha.xiaohashu.oss.biz.exception;

import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.oss.biz.enums.ResponseCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Response<?> handleOtherException(HttpServletRequest request,Exception e){
        log.error("{} 请求出错",request.getRequestURI(),e);
        return Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }

    public Response<Object> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        //参数错误异常码
        log.error("{} request error, ", request.getRequestURI(), e);
        String errorCode = ResponseCodeEnum.PARAM_NOT_VALID.getErrorCode();

        //错误信息
        String errorMessage = e.getMessage();
        log.warn("{} request error, {}, {}", request.getRequestURI(), errorCode, errorMessage);
        return Response.fail(errorCode, errorMessage);
    }
}
