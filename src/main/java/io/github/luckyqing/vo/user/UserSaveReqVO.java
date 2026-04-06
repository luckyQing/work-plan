package io.github.luckyqing.vo.user;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 用户新增/修改请求参数
 */
@Data
public class UserSaveReqVO {

    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 新增时必传，修改时为空则不更新 */
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String realName;

    /** 所属部门（字典值） */
    private String dept;

    private String role;

    private Integer status;
}
