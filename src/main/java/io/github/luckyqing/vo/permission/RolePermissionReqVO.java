package io.github.luckyqing.vo.permission;

import lombok.Data;
import java.util.List;

/** 保存角色权限请求 */
@Data
public class RolePermissionReqVO {
    private Long roleId;
    private List<Long> permissionIds;
}
