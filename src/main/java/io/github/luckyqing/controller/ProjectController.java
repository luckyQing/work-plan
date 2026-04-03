package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.vo.project.ProjectRespVO;
import io.github.luckyqing.vo.project.ProjectSaveReqVO;
import io.github.luckyqing.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目管理接口
 * 提供项目的增删改查功能
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 查询所有项目
     *
     * @return 项目列表
     */
    @GetMapping("/list")
    public R<List<ProjectRespVO>> list() {
        return R.ok(projectService.listProjects());
    }

    /**
     * 新增项目
     *
     * @param reqVO 项目信息
     * @return 操作结果
     */
    @PostMapping
    public R<Void> add(@RequestBody ProjectSaveReqVO reqVO) {
        projectService.addProject(reqVO);
        return R.ok();
    }

    /**
     * 更新项目信息
     *
     * @param reqVO 项目信息
     * @return 操作结果
     */
    @PutMapping
    public R<Void> update(@RequestBody ProjectSaveReqVO reqVO) {
        projectService.updateProject(reqVO);
        return R.ok();
    }

    /**
     * 删除项目（逻辑删除）
     *
     * @param id 项目ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        projectService.removeById(id);
        return R.ok();
    }
}
