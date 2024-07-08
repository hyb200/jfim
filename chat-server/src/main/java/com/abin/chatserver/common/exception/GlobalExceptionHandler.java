package com.abin.chatserver.common.exception;

import com.abin.chatserver.common.domain.enums.ErrorEnum;
import com.abin.chatserver.common.domain.vo.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        System.out.println(e.getMessage());
        StringBuilder errMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(x -> errMsg.append(x.getDefaultMessage()).append(","));
        String message = errMsg.substring(0, errMsg.length() - 1);
        log.error("请求参数错误: {}", message);
        return BaseResponse.error(ErrorEnum.PARAM_ERROR.getCode(), message);
    }

    @ExceptionHandler(value = Throwable.class)
    public BaseResponse<?> defaultExceptionHandler(Throwable e) {
        log.error("系统错误: {}", e.getMessage());
        return BaseResponse.error(ErrorEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(RuntimeException e) {
        log.info("业务异常: {}", e.getMessage());
        return BaseResponse.error(ErrorEnum.BUSINESS_ERROR.getCode(), e.getMessage());
    }
}
