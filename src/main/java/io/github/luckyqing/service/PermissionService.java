package io.github.luckyqing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.luckyqing.entity.PermissionEntity;
import io.github.luckyqing.entity.RoleEntity;
import io.github.luckyqing.entity.RolePermissionEntity;
import io.github.luckyqing.entity.UserRoleEntity;
import io.github.luckyqing.resposity.PermissionResposity;
import io.github.luckyqing.resposity.RoleResposity;
import io.github.luckyqing.resposity.RolePermissionResposity;
import io.github.luckyqing.resposity.UserRoleResposity;
import io.github.luckyqing.vo.permission.PermissionRespVO;
import io.github.luckyqing.vo.permission.PermissionSaveReqVO;
import io.github.luckyqing.vo.permission.RoleRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 权限服务
 * 权限信息缓存到 Redis，key 格式：
 *   perm:roles:{userId}  -> Set<String> 角色码
 *   perm:codes:{userId}  -> Set<String> 权限码
 *   perm:menus:{userId}  -> List<PermissionEntity> 菜单权限
 */
@Service
public class PermissionService {

    private static final String KEY_ROLES = "perm:roles:";
    private static final String KEY_CODES = "perm:codes:";
    private static final String KEY_MENUS = "perm:menus:";
    private static final long CACHE_TTL = 24 * 60; // 24小时（分钟）

    @Autowired
    private PermissionResposity permissionResposity;
    @Autowired
    private RoleResposity roleResposity;
    @Autowired
    private RolePermissionResposity rolePermissionResposity;
    @Autowired
    private UserRoleResposity userRoleResposity;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /** 获取用户角色码集合（优先从 Redis 取） */
    @SuppressWarnings("unchecked")
    public Set<String> getUserRoles(Long userId) {
        String key = KEY_ROLES + userId;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Set) {
            return (Set<String>) cached;
        }
        List<String> roles = permissionResposity.selectRoleCodesByUserId(userId);
        Set<String> roleSet = new HashSet<>(roles);
        redisTemplate.opsForValue().set(key, roleSet, CACHE_TTL, TimeUnit.MINUTES);
        return roleSet;
    }

    /** 获取用户权限码集合（优先从 Redis 取） */
    @SuppressWarnings("unchecked")
    public Set<String> getUserPermCodes(Long userId) {
        String key = KEY_CODES + userId;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Set) {
            return (Set<String>) cached;
        }
        List<PermissionEntity> perms = permissionResposity.selectByUserId(userId);
        Set<String> codes = perms.stream().map(PermissionEntity::getPermCode).collect(Collectors.toSet());
        redisTemplate.opsForValue().set(key, codes, CACHE_TTL, TimeUnit.MINUTES);
        return codes;
    }

    /** 获取用户菜单权限列表 */
    public List<PermissionEntity> getUserMenus(Long userId) {
        List<PermissionEntity> perms = permissionResposity.selectByUserId(userId);
        return perms.stream()
                .filter(p -> "menu".equals(p.getPermType()))
                .sorted(Comparator.comparingInt(PermissionEntity::getSortOrder))
                .collect(Collectors.toList());
    }

    /** 清除用户权限缓存（修改角色/权限后调用） */
    public void clearUserCache(Long userId) {
        redisTemplate.delete(KEY_ROLES + userId);
        redisTemplate.delete(KEY_CODES + userId);
        redisTemplate.delete(KEY_MENUS + userId);
    }

    /** 查询所有权限 */
    public List<PermissionRespVO> listAllPermissions() {
        return permissionResposity.list(new LambdaQueryWrapper<PermissionEntity>()
                .eq(PermissionEntity::getDeleted, 0)
                .orderByAsc(PermissionEntity::getSortOrder))
                .stream().map(this::toPermVO).collect(Collectors.toList());
    }

    /** 查询所有角色 */
    public List<RoleRespVO> listAllRoles() {
        return roleResposity.list(new LambdaQueryWrapper<RoleEntity>()
                .eq(RoleEntity::getDeleted, 0))
                .stream().map(this::toRoleVO).collect(Collectors.toList());
    }

    /** 查询角色拥有的权限ID列表 */
    public List<Long> getRolePermissionIds(Long roleId) {
        return rolePermissionResposity.list(
                new LambdaQueryWrapper<RolePermissionEntity>().eq(RolePermissionEntity::getRoleId, roleId))
                .stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
    }

    /** 保存角色权限（先删后插） */
    public void saveRolePermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionResposity.remove(new LambdaQueryWrapper<RolePermissionEntity>()
                .eq(RolePermissionEntity::getRoleId, roleId));
        if (permissionIds != null && !permissionIds.isEmpty()) {
            permissionIds.forEach(pid -> {
                RolePermissionEntity rp = new RolePermissionEntity();
                rp.setRoleId(roleId);
                rp.setPermissionId(pid);
                rolePermissionResposity.save(rp);
            });
        }
        // 清除该角色下所有用户的缓存
        userRoleResposity.list(new LambdaQueryWrapper<UserRoleEntity>().eq(UserRoleEntity::getRoleId, roleId))
                .forEach(ur -> clearUserCache(ur.getUserId()));
    }

    /** 查询用户角色ID列表 */
    public List<Long> getUserRoleIds(Long userId) {
        return userRoleResposity.list(
                new LambdaQueryWrapper<UserRoleEntity>().eq(UserRoleEntity::getUserId, userId))
                .stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
    }

    /** 保存用户角色（先删后插） */
    public void saveUserRoles(Long userId, List<Long> roleIds) {
        userRoleResposity.remove(new LambdaQueryWrapper<UserRoleEntity>().eq(UserRoleEntity::getUserId, userId));
        if (roleIds != null && !roleIds.isEmpty()) {
            roleIds.forEach(rid -> {
                UserRoleEntity ur = new UserRoleEntity();
                ur.setUserId(userId);
                ur.setRoleId(rid);
                userRoleResposity.save(ur);
            });
        }
        clearUserCache(userId);
    }

    private PermissionRespVO toPermVO(PermissionEntity p) {
        PermissionRespVO vo = new PermissionRespVO();
        vo.setId(p.getId());
        vo.setPermCode(p.getPermCode());
        vo.setPermName(p.getPermName());
        vo.setPermType(p.getPermType());
        vo.setPermPath(p.getPermPath());
        vo.setParentId(p.getParentId());
        vo.setSortOrder(p.getSortOrder());
        vo.setStatus(p.getStatus());
        return vo;
    }

    private RoleRespVO toRoleVO(RoleEntity r) {
        RoleRespVO vo = new RoleRespVO();
        vo.setId(r.getId());
        vo.setRoleCode(r.getRoleCode());
        vo.setRoleName(r.getRoleName());
        vo.setDescription(r.getDescription());
        vo.setStatus(r.getStatus());
        return vo;
    }

    /** 新增角色 */
    public void addRole(io.github.luckyqing.vo.permission.RoleSaveReqVO reqVO) {
        RoleEntity role = new RoleEntity();
        role.setRoleCode(reqVO.getRoleCode());
        role.setRoleName(reqVO.getRoleName());
        role.setDescription(reqVO.getDescription());
        role.setStatus(reqVO.getStatus() != null ? reqVO.getStatus() : 1);
        roleResposity.save(role);
    }

    /** 修改角色 */
    public void updateRole(io.github.luckyqing.vo.permission.RoleSaveReqVO reqVO) {
        RoleEntity role = new RoleEntity();
        role.setId(reqVO.getId());
        role.setRoleCode(reqVO.getRoleCode());
        role.setRoleName(reqVO.getRoleName());
        role.setDescription(reqVO.getDescription());
        role.setStatus(reqVO.getStatus());
        roleResposity.updateById(role);
        // 清除该角色下所有用户的缓存
        userRoleResposity.list(new LambdaQueryWrapper<UserRoleEntity>().eq(UserRoleEntity::getRoleId, reqVO.getId()))
                .forEach(ur -> clearUserCache(ur.getUserId()));
    }

    /** 删除角色（逻辑删除） */
    public void deleteRole(Long roleId) {
        roleResposity.removeById(roleId);
        userRoleResposity.list(new LambdaQueryWrapper<UserRoleEntity>().eq(UserRoleEntity::getRoleId, roleId))
                .forEach(ur -> clearUserCache(ur.getUserId()));
    }

    /** 新增权限 */
    public void addPermission(PermissionSaveReqVO reqVO) {
        PermissionEntity p = toEntity(reqVO);
        permissionResposity.save(p);
    }

    /** 修改权限 */
    public void updatePermission(PermissionSaveReqVO reqVO) {
        PermissionEntity p = toEntity(reqVO);
        permissionResposity.updateById(p);
        // 权限变更后清除所有用户缓存（简单处理）
        redisTemplate.keys("perm:*").forEach(k -> redisTemplate.delete(k));
    }

    /** 删除权限（逻辑删除） */
    public void deletePermission(Long permId) {
        permissionResposity.removeById(permId);
        // 同步删除角色-权限关联
        rolePermissionResposity.remove(new LambdaQueryWrapper<RolePermissionEntity>()
                .eq(RolePermissionEntity::getPermissionId, permId));
        redisTemplate.keys("perm:*").forEach(k -> redisTemplate.delete(k));
    }

    private PermissionEntity toEntity(PermissionSaveReqVO reqVO) {
        PermissionEntity p = new PermissionEntity();
        p.setId(reqVO.getId());
        p.setPermCode(reqVO.getPermCode());
        p.setPermName(reqVO.getPermName());
        p.setPermType(reqVO.getPermType());
        p.setPermPath(reqVO.getPermPath());
        p.setParentId(reqVO.getParentId() != null ? reqVO.getParentId() : 0L);
        p.setSortOrder(reqVO.getSortOrder() != null ? reqVO.getSortOrder() : 0);
        p.setStatus(reqVO.getStatus() != null ? reqVO.getStatus() : 1);
        return p;
    }
}
