package io.github.luckyqing.test;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import io.github.luckyqing.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.nio.file.Paths;
import java.util.Collections;

@Slf4j
public class MybatisPlusGenerateTest {

    @Test
    public void test() {
        GenerateCodeDTO generateCodeDTO = GenerateCodeDTO.builder()
                .url("jdbc:mysql://10.60.8.31:3308/work_plan?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai")
                .username("root")
                .password("YOUxin@com")
                .author("collin.li")
                .parent("io.github.luckyqing")
                .tables(new String[]{"t_task"})
                .build();
        generateCode(generateCodeDTO);
    }

    /**
     * 生成代码
     *
     * @param dto
     */
    public void generateCode(GenerateCodeDTO dto) {
        FastAutoGenerator.create(dto.getUrl(), dto.getUsername(), dto.getPassword())
                .dataSourceConfig(builder -> builder
                        .typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            if (JdbcType.TINYINT == metaInfo.getJdbcType()
                                    || JdbcType.SMALLINT == metaInfo.getJdbcType()) {
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .globalConfig(builder -> builder
                        .author(dto.getAuthor())
                        .disableServiceInterface()
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
                        //.outputDir("d:/generate-code")
                        .commentDate("yyyy-MM-dd")
                        .disableOpenDir()
                )
                .packageConfig(builder -> builder
                        .parent(dto.getParent())
                        .entity("entity")
                        .mapper("mapper")
                        .service(null)
                        .serviceImpl("resposity")
                        .controller(null)
                        .xml("mapper")
                        .pathInfo(Collections.singletonMap(OutputFile.xml,
                                Paths.get(System.getProperty("user.dir")) + "/src/main/resources/mapper"))
                )
                .strategyConfig(builder -> {
                    builder.addFieldPrefix("f_", "t_")
                        .addTablePrefix("t_");
                    if (ArrayUtil.isNotEmpty(dto.getTables())) {
                        builder.addInclude(dto.getTables());
                    }
                    builder
                        .serviceBuilder().formatServiceImplFileName("%sResposity").fileOverride()
                        .mapperBuilder().formatMapperFileName("%sMapper").formatXmlFileName("%sMapper").fileOverride()
                        .entityBuilder()
                        .superClass(BaseEntity.class)
                        .addSuperEntityColumns("id", "create_id", "update_id", "create_time", "update_time", "deleted")
                        .disableSerialVersionUID()
                        .formatFileName("%sEntity")
                        .enableTableFieldAnnotation()
                        .enableLombok()
                        .logicDeleteColumnName("deleted")
                        .fileOverride();
                }).templateConfig(builder -> builder
                        .disable(TemplateType.CONTROLLER)
                        .disable(TemplateType.SERVICE)
                        .serviceImpl("/templates/serviceImpl.java")
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    @Getter
    @Builder
    private static class GenerateCodeDTO {
        /**
         * 数据库url
         */
        @NotNull
        private String url;
        /**
         * 数据库用户名
         */
        @NotNull
        private String username;
        /**
         * 数据库密码
         */
        @NotNull
        private String password;
        /**
         * 编码人
         */
        private String author;
        /**
         * package父包
         */
        @NotNull
        private String parent;
        /**
         * 要生成的表名（不设置表示生成所有表）
         */
        @Nullable
        private String[] tables;
    }

}