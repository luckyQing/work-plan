package io.github.luckyqing.vo.config;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 配置项新增/修改请求参数
 */
@Data
public class ConfigSaveReqVO {

    private Long id;

    @NotBlank(message = "配置类别不能为空")
    private String configType;

    @NotBlank(message = "显示名称不能为空")
    private String configLabel;

    @NotBlank(message = "配置值不能为空")
    private String configValue;

    private Integer sortOrder;

    private Integer status;
}
