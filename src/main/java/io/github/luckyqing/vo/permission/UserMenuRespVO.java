package io.github.luckyqing.vo.permission;

import lombok.Data;

/** 用户菜单权限（前端用于动态渲染导航） */
@Data
public class UserMenuRespVO {
    private Long id;
    private String permCode;
    private String permName;
    private String permPath;
    private Long parentId;
    private Integer sortOrder;
}
