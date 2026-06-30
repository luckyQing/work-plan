package io.github.luckyqing.resposity;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.RolePermissionEntity;
import io.github.luckyqing.mapper.RolePermissionMapper;
import org.springframework.stereotype.Service;

/**
 * 角色-权限关联数据访问
 */
@Service
public class RolePermissionResposity extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> {
}
