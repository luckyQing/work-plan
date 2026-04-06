package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.vo.config.ConfigRespVO;
import io.github.luckyqing.vo.config.ConfigSaveReqVO;
import io.github.luckyqing.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 系统配置接口
 * 提供配置项的增删改查，支持按类别查询
 */
@RestController
@RequestMapping("/api/config")
public class SysConfigController {

    @Autowired
    private DictService configService;

    /**
     * 按类别查询配置项（按排序号升序）
     *
     * @param configType 配置类别
     * @return 配置项列表
     */
    @GetMapping("/list")
    public R<List<ConfigRespVO>> listByType(@RequestParam String configType) {
        return R.ok(configService.listByType(configType));
    }

    /**
     * 查询所有配置项，按类别分组
     *
     * @return Map<类别, 配置项列表>
     */
    @GetMapping("/all")
    public R<Map<String, List<ConfigRespVO>>> listAll() {
        return R.ok(configService.listAllGrouped());
    }

    /**
     * 查询所有配置项（平铺列表，管理页面用）
     */
    @GetMapping("/flatList")
    public R<List<ConfigRespVO>> flatList() {
        return R.ok(configService.listAll());
    }

    /**
     * 新增配置项
     */
    @PostMapping
    public R<Void> add(@Valid @RequestBody ConfigSaveReqVO reqVO) {
        configService.addConfig(reqVO);
        return R.ok();
    }

    /**
     * 更新配置项
     */
    @PutMapping
    public R<Void> update(@Valid @RequestBody ConfigSaveReqVO reqVO) {
        configService.updateConfig(reqVO);
        return R.ok();
    }

    /**
     * 删除配置项
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        configService.removeById(id);
        return R.ok();
    }
}
