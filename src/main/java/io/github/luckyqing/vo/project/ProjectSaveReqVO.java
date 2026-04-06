package io.github.luckyqing.vo.project;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 项目新增/修改请求参数
 */
@Data
public class ProjectSaveReqVO {

    private Long id;

    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    private Integer status;
}
