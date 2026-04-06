package io.github.luckyqing.vo.permission;

import lombok.Data;

@Data
public class PermissionRespVO {
    private Long id;
    private String permCode;
    private String permName;
    private String permType;
    private String permPath;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
}
