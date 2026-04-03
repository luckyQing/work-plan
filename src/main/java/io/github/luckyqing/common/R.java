package io.github.luckyqing.common;

import lombok.Data;

/**
 * 统一响应结果封装
 *
 * @param <T> 响应数据类型
 */
@Data
public class R<T> {

    /** 状态码: 200成功, 401未登录, 500失败 */
    private int code;

    /** 提示信息 */
    private String msg;

    /** 响应数据 */
    private T data;

    /**
     * 成功（带数据）
     */
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    /**
     * 成功（无数据）
     */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /**
     * 失败（默认500）
     */
    public static <T> R<T> fail(String msg) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    /**
     * 失败（自定义状态码）
     */
    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}
