package io.github.luckyqing.vo.permission;

import lombok.Data;

@Data
public class RoleRespVO {
    private Long id;
    private String roleCode;
    private String roleName;
    private String description;
    private Integer status;
}
