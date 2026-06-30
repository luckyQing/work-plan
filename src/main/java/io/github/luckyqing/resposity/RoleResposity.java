package io.github.luckyqing.resposity;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.RoleEntity;
import io.github.luckyqing.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * 角色数据访问
 */
@Service
public class RoleResposity extends ServiceImpl<RoleMapper, RoleEntity> {
}
