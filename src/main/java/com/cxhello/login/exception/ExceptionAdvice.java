package com.cxhello.login.exception;

import com.cxhello.login.util.Result;
import com.cxhello.login.util.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> validExceptionHandler(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return ResultUtils.result(HttpStatus.INTERNAL_SERVER_ERROR.value(), objectError.getDefaultMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<Object> validExceptionHandler(ConstraintViolationException e) {
        ConstraintViolation<?> next = e.getConstraintViolations().iterator().next();
        return ResultUtils.result(HttpStatus.INTERNAL_SERVER_ERROR.value(), next.getMessage());
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public Result<Object> validExceptionHandler(BadCredentialsException e) {
        logger.error(e.getMessage(), e);
        return ResultUtils.result(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户名或密码不正确");
    }

    @ExceptionHandler(value = BusinessException.class)
    public Result<Object> validExceptionHandler(BusinessException e) {
        return ResultUtils.result(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResultUtils.result(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

}
