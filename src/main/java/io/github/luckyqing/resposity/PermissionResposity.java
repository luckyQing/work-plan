package io.github.luckyqing.resposity;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.PermissionEntity;
import io.github.luckyqing.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限数据访问
 */
@Service
public class PermissionResposity extends ServiceImpl<PermissionMapper, PermissionEntity> {

    /** 查询用户拥有的所有权限（通过角色关联） */
    public List<PermissionEntity> selectByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    /** 查询用户拥有的角色编码列表 */
    public List<String> selectRoleCodesByUserId(Long userId) {
        return baseMapper.selectRoleCodesByUserId(userId);
    }
}
