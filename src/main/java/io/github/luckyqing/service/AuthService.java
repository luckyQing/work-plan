package io.github.luckyqing.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.luckyqing.common.JwtUtil;
import io.github.luckyqing.common.R;
import io.github.luckyqing.entity.UserEntity;
import io.github.luckyqing.resposity.UserResposity;
import io.github.luckyqing.vo.auth.LoginReqVO;
import io.github.luckyqing.vo.auth.LoginRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证服务
 * 使用 JWT 生成和验证 token
 */
@Service
public class AuthService {

    @Autowired
    private UserResposity userResposity;

    /**
     * 用户登录
     */
    public R<LoginRespVO> login(LoginReqVO reqVO) {
        if (reqVO.getUsername() == null || reqVO.getUsername().isEmpty()) {
            return R.fail("用户名不能为空");
        }
        if (reqVO.getPassword() == null || reqVO.getPassword().isEmpty()) {
            return R.fail("密码不能为空");
        }

        UserEntity user = userResposity.getOne(
                new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, reqVO.getUsername()));
        if (user == null) {
            return R.fail("用户不存在");
        }
        if (user.getStatus() != 1) {
            return R.fail("账号已禁用");
        }
        if (!DigestUtil.md5Hex(reqVO.getPassword()).equals(user.getPassword())) {
            return R.fail("密码错误");
        }

        // 生成 JWT token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        LoginRespVO respVO = new LoginRespVO();
        respVO.setToken(token);
        respVO.setUserId(user.getId());
        respVO.setRealName(user.getRealName());
        respVO.setRole(user.getRole());
        return R.ok(respVO);
    }

    /**
     * 用户登出（JWT 无状态，客户端清除 token 即可）
     */
    public R<Void> logout() {
        return R.ok();
    }
}
