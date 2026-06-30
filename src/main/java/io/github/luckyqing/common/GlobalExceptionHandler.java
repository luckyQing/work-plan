package io.github.luckyqing.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

/**
 * 全局异常处理
 * 统一返回 {@link R} 结构，向前端隐藏异常堆栈与内部细节；
 * 服务端日志中尽量打印请求 URL、请求参数与完整异常堆栈，便于排查。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fe = e.getBindingResult().getFieldError();
        String msg = fe != null ? fe.getDefaultMessage() : "参数校验失败";
        log.warn("参数校验失败 {} 参数={} 请求体={} 原因={}", reqLine(request), params(request), body(request), msg);
        return R.fail(msg);
    }

    @ExceptionHandler(BindException.class)
    public R<Void> handleBind(BindException e, HttpServletRequest request) {
        FieldError fe = e.getBindingResult().getFieldError();
        String msg = fe != null ? fe.getDefaultMessage() : "参数校验失败";
        log.warn("参数绑定失败 {} 参数={} 请求体={} 原因={}", reqLine(request), params(request), body(request), msg);
        return R.fail(msg);
    }

    /** 参数非法（IllegalArgumentException 等），消息为业务自定义、可安全展示 */
    @ExceptionHandler(IllegalArgumentException.class)
    public R<Void> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("参数非法 {} 参数={} 请求体={} 原因={}", reqLine(request), params(request), body(request), e.getMessage());
        return R.fail(e.getMessage() != null ? e.getMessage() : "参数错误");
    }

    /** Shiro 无权限异常 */
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public R<Void> handleUnauthorized(Exception e, HttpServletRequest request) {
        log.warn("无权限访问 {} 参数={}", reqLine(request), params(request));
        return R.fail(403, "无权限访问");
    }

    /**
     * 兜底异常处理。
     * 请求 URL、请求参数、请求体与完整堆栈写入服务端日志，前端只收到通用提示，避免泄露内部实现细节。
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 {} 参数={} 请求体={}", reqLine(request), params(request), body(request), e);
        return R.fail("系统异常，请稍后重试或联系管理员");
    }

    /** 拼接请求方法与完整 URL（含 query string） */
    private String reqLine(HttpServletRequest request) {
        if (request == null) {
            return "[无请求上下文]";
        }
        String query = request.getQueryString();
        return request.getMethod() + " " + request.getRequestURL()
                + (query != null ? "?" + query : "");
    }

    /** 收集请求参数（表单/查询参数），多值用逗号连接，无参数时返回 {} */
    private String params(HttpServletRequest request) {
        if (request == null) {
            return "{}";
        }
        Map<String, String[]> map = request.getParameterMap();
        if (map.isEmpty()) {
            return "{}";
        }
        return map.entrySet().stream()
                .map(en -> en.getKey() + "=" + String.join(",", en.getValue()))
                .collect(Collectors.joining("&", "{", "}"));
    }

    /** 读取已缓存的请求体（如 JSON body），最多截取 2000 字符，无法读取时返回空串 */
    private String body(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper == null) {
            return "";
        }
        byte[] buf = wrapper.getContentAsByteArray();
        if (buf.length == 0) {
            return "";
        }
        String charset = wrapper.getCharacterEncoding();
        String content = new String(buf, charset != null
                ? java.nio.charset.Charset.forName(charset) : StandardCharsets.UTF_8);
        return content.length() > 2000 ? content.substring(0, 2000) + "...(truncated)" : content;
    }
}
