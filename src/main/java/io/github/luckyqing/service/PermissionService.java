package io.github.luckyqing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.luckyqing.entity.Permission;
import io.github.luckyqing.entity.Role;
import io.github.luckyqing.entity.RolePermission;
import io.github.luckyqing.entity.UserRole;
import io.github.luckyqing.mapper.PermissionMapper;
import io.github.luckyqing.mapper.RoleMapper;
import io.github.luckyqing.mapper.RolePermissionMapper;
import io.github.luckyqing.mapper.UserRoleMapper;
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
 *   perm:menus:{userId}  -> List<Permission> 菜单权限
 */
@Service
public class PermissionService {

    private static final String KEY_ROLES = "perm:roles:";
    private static final String KEY_CODES = "perm:codes:";
    private static final String KEY_MENUS = "perm:menus:";
    private static final long CACHE_TTL = 24 * 60; // 24小时（分钟）

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /** 获取用户角色码集合（优先从 Redis 取） */
    @SuppressWarnings("unchecked")
    public Set<String> getUserRoles(Long userId) {
        String key = KEY_ROLES + userId;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Set) return (Set<String>) cached;
        List<String> roles = permissionMapper.selectRoleCodesByUserId(userId);
        Set<String> roleSet = new HashSet<>(roles);
        redisTemplate.opsForValue().set(key, roleSet, CACHE_TTL, TimeUnit.MINUTES);
        return roleSet;
    }

    /** 获取用户权限码集合（优先从 Redis 取） */
    @SuppressWarnings("unchecked")
    public Set<String> getUserPermCodes(Long userId) {
        String key = KEY_CODES + userId;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Set) return (Set<String>) cached;
        List<Permission> perms = permissionMapper.selectByUserId(userId);
        Set<String> codes = perms.stream().map(Permission::getPermCode).collect(Collectors.toSet());
        redisTemplate.opsForValue().set(key, codes, CACHE_TTL, TimeUnit.MINUTES);
        return codes;
    }

    /** 获取用户菜单权限列表 */
    public List<Permission> getUserMenus(Long userId) {
        List<Permission> perms = permissionMapper.selectByUserId(userId);
        return perms.stream()
                .filter(p -> "menu".equals(p.getPermType()))
                .sorted(Comparator.comparingInt(Permission::getSortOrder))
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
        return permissionMapper.selectList(new LambdaQueryWrapper<Permission>()
                .eq(Permission::getDeleted, 0)
                .orderByAsc(Permission::getSortOrder))
                .stream().map(this::toPermVO).collect(Collectors.toList());
    }

    /** 查询所有角色 */
    public List<RoleRespVO> listAllRoles() {
        return roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .eq(Role::getDeleted, 0))
                .stream().map(this::toRoleVO).collect(Collectors.toList());
    }

    /** 查询角色拥有的权限ID列表 */
    public List<Long> getRolePermissionIds(Long roleId) {
        return rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId))
                .stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
    }

    /** 保存角色权限（先删后插） */
    public void saveRolePermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getRoleId, roleId));
        if (permissionIds != null && !permissionIds.isEmpty()) {
            permissionIds.forEach(pid -> {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(pid);
                rolePermissionMapper.insert(rp);
            });
        }
        // 清除该角色下所有用户的缓存
        userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId))
                .forEach(ur -> clearUserCache(ur.getUserId()));
    }

    /** 查询用户角色ID列表 */
    public List<Long> getUserRoleIds(Long userId) {
        return userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }

    /** 保存用户角色（先删后插） */
    public void saveUserRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        if (roleIds != null && !roleIds.isEmpty()) {
            roleIds.forEach(rid -> {
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(rid);
                userRoleMapper.insert(ur);
            });
        }
        clearUserCache(userId);
    }

    private PermissionRespVO toPermVO(Permission p) {
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

    private RoleRespVO toRoleVO(Role r) {
        RoleRespVO vo = new RoleRespVO();
        vo.setId(r.getId());
        vo.setRoleCode(r.getRoleCode());
        vo.setRoleName(r.getRoleName());
        vo.setDescription(r.getDescription());
        vo.setStatus(r.getStatus());
        return vo;
    }

    /** 新增角色 */
    public void addRole(io.github.luckyqing.vo.permission.RoleSaveReqVO reqVO) {        Role role = new Role();
        role.setRoleCode(reqVO.getRoleCode());
        role.setRoleName(reqVO.getRoleName());
        role.setDescription(reqVO.getDescription());
        role.setStatus(reqVO.getStatus() != null ? reqVO.getStatus() : 1);
        roleMapper.insert(role);
    }

    /** 修改角色 */
    public void updateRole(io.github.luckyqing.vo.permission.RoleSaveReqVO reqVO) {
        Role role = new Role();
        role.setId(reqVO.getId());
        role.setRoleCode(reqVO.getRoleCode());
        role.setRoleName(reqVO.getRoleName());
        role.setDescription(reqVO.getDescription());
        role.setStatus(reqVO.getStatus());
        roleMapper.updateById(role);
        // 清除该角色下所有用户的缓存
        userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, reqVO.getId()))
                .forEach(ur -> clearUserCache(ur.getUserId()));
    }

    /** 删除角色（逻辑删除） */
    public void deleteRole(Long roleId) {
        roleMapper.deleteById(roleId);
        userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId))
                .forEach(ur -> clearUserCache(ur.getUserId()));
    }

    /** 新增权限 */
    public void addPermission(PermissionSaveReqVO reqVO) {
        Permission p = toEntity(reqVO);
        permissionMapper.insert(p);
    }

    /** 修改权限 */
    public void updatePermission(PermissionSaveReqVO reqVO) {
        Permission p = toEntity(reqVO);
        permissionMapper.updateById(p);
        // 权限变更后清除所有用户缓存（简单处理）
        redisTemplate.keys("perm:*").forEach(k -> redisTemplate.delete(k));
    }

    /** 删除权限（逻辑删除） */
    public void deletePermission(Long permId) {
        permissionMapper.deleteById(permId);
        // 同步删除角色-权限关联
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>()
                .eq(RolePermission::getPermissionId, permId));
        redisTemplate.keys("perm:*").forEach(k -> redisTemplate.delete(k));
    }

    private Permission toEntity(PermissionSaveReqVO reqVO) {
        Permission p = new Permission();
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
