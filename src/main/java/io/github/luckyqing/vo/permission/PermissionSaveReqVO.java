package io.github.luckyqing.vo.permission;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/** 权限新增/修改请求参数 */
@Data
public class PermissionSaveReqVO {

    private Long id;

    @NotBlank(message = "权限标识不能为空")
    private String permCode;

    @NotBlank(message = "权限名称不能为空")
    private String permName;

    @NotBlank(message = "权限类型不能为空")
    private String permType;

    private String permPath;

    private Long parentId;

    private Integer sortOrder;

    private Integer status;
}
