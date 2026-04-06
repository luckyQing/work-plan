package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.vo.auth.LoginReqVO;
import io.github.luckyqing.vo.auth.LoginRespVO;
import io.github.luckyqing.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /** 用户登录 */
    @PostMapping("/login")
    public R<LoginRespVO> login(@Valid @RequestBody LoginReqVO reqVO) {
        return authService.login(reqVO);
    }

    /** 用户登出（JWT 无状态，客户端清除 token 即可） */
    @PostMapping("/logout")
    public R<Void> logout() {
        return authService.logout();
    }
}
