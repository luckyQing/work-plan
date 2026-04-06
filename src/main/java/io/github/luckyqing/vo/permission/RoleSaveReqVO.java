package io.github.luckyqing.vo.permission;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/** 角色新增/修改请求参数 */
@Data
public class RoleSaveReqVO {

    private Long id;

    @NotBlank(message = "角色标识不能为空")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    private String description;

    private Integer status;
}
