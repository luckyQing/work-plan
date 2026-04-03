package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.vo.auth.LoginReqVO;
import io.github.luckyqing.vo.auth.LoginRespVO;
import io.github.luckyqing.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证接口
 * 提供登录、登出功能
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     *
     * @param reqVO 登录参数（用户名、密码）
     * @return 登录结果（token、用户信息）
     */
    @PostMapping("/login")
    public R<LoginRespVO> login(@RequestBody LoginReqVO reqVO) {
        return authService.login(reqVO);
    }

    /**
     * 用户登出
     * 清除 Redis 中的 token
     *
     * @param request HTTP请求（从请求头获取 Authorization）
     * @return 操作结果
     */
    @PostMapping("/logout")
    public R<Void> logout(HttpServletRequest request) {
        return authService.logout(request.getHeader("Authorization"));
    }
}
