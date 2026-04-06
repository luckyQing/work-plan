package io.github.luckyqing.common;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * 统一处理参数校验异常，返回友好提示
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** @RequestBody 参数校验失败 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidation(MethodArgumentNotValidException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        String msg = fe != null ? fe.getDefaultMessage() : "参数校验失败";
        return R.fail(msg);
    }

    /** 表单参数校验失败 */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBind(BindException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        String msg = fe != null ? fe.getDefaultMessage() : "参数校验失败";
        return R.fail(msg);
    }

    /** 通用异常 */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        return R.fail("系统异常: " + e.getMessage());
    }
}
