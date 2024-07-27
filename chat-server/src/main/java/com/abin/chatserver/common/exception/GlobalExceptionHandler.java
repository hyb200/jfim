package com.abin.chatserver.common.exception;

import com.abin.chatserver.common.domain.enums.ErrorEnum;
import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        System.out.println(e.getMessage());
        StringBuilder errMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(x -> errMsg.append(x.getDefaultMessage()).append(","));
        String message = errMsg.substring(0, errMsg.length() - 1);
        log.error("请求参数错误: {}", message);
        return BaseResponse.error(ErrorEnum.PARAM_ERROR.getCode(), message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public BaseResponse bindException(BindException e) {
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(x.getDefaultMessage()).append(","));
        String message = errorMsg.substring(0, errorMsg.length() - 1);
        log.info("validation parameters error！The reason is:{}", message);
        return BaseResponse.error(ErrorEnum.PARAM_ERROR.getCode(), message);
    }

    @ExceptionHandler(value = Throwable.class)
    public BaseResponse<?> defaultExceptionHandler(Throwable e) {
        log.error("系统错误: {}", e.getMessage());
        return BaseResponse.error(ErrorEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(RuntimeException e) {
        log.info("业务异常: {}", e.getMessage());
        return BaseResponse.error(ErrorEnum.BUSINESS_ERROR.getCode(), e.getMessage());
    }
}
