package io.github.luckyqing.common;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidation(MethodArgumentNotValidException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        return R.fail(fe != null ? fe.getDefaultMessage() : "参数校验失败");
    }

    @ExceptionHandler(BindException.class)
    public R<Void> handleBind(BindException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        return R.fail(fe != null ? fe.getDefaultMessage() : "参数校验失败");
    }

    /** Shiro 无权限异常 */
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public R<Void> handleUnauthorized(Exception e) {
        return R.fail(403, "无权限访问");
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        return R.fail("系统异常: " + e.getMessage());
    }
}
