package io.github.luckyqing.resposity;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.UserRoleEntity;
import io.github.luckyqing.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * 用户-角色关联数据访问
 */
@Service
public class UserRoleResposity extends ServiceImpl<UserRoleMapper, UserRoleEntity> {
}
