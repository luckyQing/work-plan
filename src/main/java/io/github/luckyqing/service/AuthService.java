package io.github.luckyqing.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.luckyqing.common.R;
import io.github.luckyqing.entity.SysUser;
import io.github.luckyqing.mapper.SysUserMapper;
import io.github.luckyqing.vo.auth.LoginReqVO;
import io.github.luckyqing.vo.auth.LoginRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务
 * 处理用户登录、登出逻辑，基于 Redis 存储 token
 */
@Service
public class AuthService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录
     * 校验用户名密码，生成 token 存入 Redis（24小时过期）
     *
     * @param reqVO 登录请求参数
     * @return 登录结果（含 token、用户信息）
     */
    public R<LoginRespVO> login(LoginReqVO reqVO) {
        // 根据用户名查询用户
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, reqVO.getUsername()));
        if (user == null) {
            return R.fail("用户不存在");
        }
        if (user.getStatus() != 1) {
            return R.fail("账号已禁用");
        }

        // 校验密码（MD5）
        String md5 = DigestUtil.md5Hex(reqVO.getPassword());
        if (!md5.equals(user.getPassword())) {
            return R.fail("密码错误");
        }

        // 生成 token 并存入 Redis，有效期24小时
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("token:" + token, user.getId(), 24, TimeUnit.HOURS);

        // 组装返回数据
        LoginRespVO respVO = new LoginRespVO();
        respVO.setToken(token);
        respVO.setUserId(user.getId());
        respVO.setRealName(user.getRealName());
        respVO.setRole(user.getRole());
        return R.ok(respVO);
    }

    /**
     * 用户登出
     * 从 Redis 中删除 token
     *
     * @param authorization 请求头中的 Authorization 值
     * @return 操作结果
     */
    public R<Void> logout(String authorization) {
        String token = authorization;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token != null) {
            redisTemplate.delete("token:" + token);
        }
        return R.ok();
    }
}
