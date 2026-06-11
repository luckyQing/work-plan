package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.common.RoleConstants;
import io.github.luckyqing.entity.Permission;
import io.github.luckyqing.service.PermissionService;
import io.github.luckyqing.vo.permission.*;
import io.github.luckyqing.vo.permission.RoleSaveReqVO;
import io.github.luckyqing.vo.permission.PermissionSaveReqVO;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理接口
 */
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /** 获取当前用户的菜单权限（前端动态导航用） */
    @GetMapping("/menus")
    public R<List<UserMenuRespVO>> myMenus(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Permission> menus = permissionService.getUserMenus(userId);
        List<UserMenuRespVO> vos = menus.stream().map(p -> {
            UserMenuRespVO vo = new UserMenuRespVO();
            vo.setId(p.getId());
            vo.setPermCode(p.getPermCode());
            vo.setPermName(p.getPermName());
            vo.setPermPath(p.getPermPath());
            vo.setParentId(p.getParentId());
            vo.setSortOrder(p.getSortOrder());
            return vo;
        }).collect(Collectors.toList());
        return R.ok(vos);
    }

    /** 查询所有权限（管理员用） */
    @GetMapping("/list")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<List<PermissionRespVO>> listAll() {
        return R.ok(permissionService.listAllPermissions());
    }

    /** 查询所有角色 */
    @GetMapping("/roles")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<List<RoleRespVO>> listRoles() {
        return R.ok(permissionService.listAllRoles());
    }

    /** 查询角色拥有的权限ID */
    @GetMapping("/role/{roleId}/permissions")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<List<Long>> getRolePermissions(@PathVariable Long roleId) {
        return R.ok(permissionService.getRolePermissionIds(roleId));
    }

    /** 保存角色权限 */
    @PostMapping("/role/permissions")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<Void> saveRolePermissions(@RequestBody RolePermissionReqVO reqVO) {
        permissionService.saveRolePermissions(reqVO.getRoleId(), reqVO.getPermissionIds());
        return R.ok();
    }

    /** 查询用户角色ID */
    @GetMapping("/user/{userId}/roles")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<List<Long>> getUserRoles(@PathVariable Long userId) {
        return R.ok(permissionService.getUserRoleIds(userId));
    }

    /** 保存用户角色 */
    @PostMapping("/user/roles")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<Void> saveUserRoles(@RequestBody UserRoleReqVO reqVO) {
        permissionService.saveUserRoles(reqVO.getUserId(), reqVO.getRoleIds());
        return R.ok();
    }

    /** 清除用户权限缓存 */
    @DeleteMapping("/cache/{userId}")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<Void> clearCache(@PathVariable Long userId) {
        permissionService.clearUserCache(userId);
        return R.ok();
    }

    /** 新增角色 */
    @PostMapping("/roles")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<Void> addRole(@Valid @RequestBody RoleSaveReqVO reqVO) {
        permissionService.addRole(reqVO);
        return R.ok();
    }

    /** 修改角色 */
    @PutMapping("/roles")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<Void> updateRole(@Valid @RequestBody RoleSaveReqVO reqVO) {
        permissionService.updateRole(reqVO);
        return R.ok();
    }

    /** 删除角色 */
    @DeleteMapping("/roles/{roleId}")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<Void> deleteRole(@PathVariable Long roleId) {
        permissionService.deleteRole(roleId);
        return R.ok();
    }

    /** 新增权限 */
    @PostMapping
    @RequiresRoles(RoleConstants.ADMIN)
    public R<Void> addPermission(@Valid @RequestBody PermissionSaveReqVO reqVO) {
        permissionService.addPermission(reqVO);
        return R.ok();
    }

    /** 修改权限 */
    @PutMapping
    @RequiresRoles(RoleConstants.ADMIN)
    public R<Void> updatePermission(@Valid @RequestBody PermissionSaveReqVO reqVO) {
        permissionService.updatePermission(reqVO);
        return R.ok();
    }

    /** 删除权限 */
    @DeleteMapping("/{permId}")
    @RequiresRoles(RoleConstants.ADMIN)
    public R<Void> deletePermission(@PathVariable Long permId) {
        permissionService.deletePermission(permId);
        return R.ok();
    }
}
