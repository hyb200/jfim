package com.abin.chatserver.common.utils;

import com.abin.chatserver.common.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

public class CheckUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 注解验证参数(全部校验,抛出异常)
     *
     * @param obj
     */
    public static <T> void allCheckValidateThrow(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if (!constraintViolations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ConstraintViolation<T> violation : constraintViolations) {
                //拼接异常信息
                errorMsg.append(violation.getPropertyPath().toString()).append(":").append(violation.getMessage()).append(",");
            }
            throw new BusinessException(errorMsg.substring(0, errorMsg.length() - 1));
        }
    }
}
