package io.github.luckyqing.vo.permission;

import lombok.Data;
import java.util.List;

/** 保存用户角色请求 */
@Data
public class UserRoleReqVO {
    private Long userId;
    private List<Long> roleIds;
}
