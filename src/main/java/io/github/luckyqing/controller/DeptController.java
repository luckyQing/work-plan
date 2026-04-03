package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.vo.dept.DeptRespVO;
import io.github.luckyqing.vo.dept.DeptSaveReqVO;
import io.github.luckyqing.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理接口
 * 提供部门的增删改查功能
 */
@RestController
@RequestMapping("/api/dept")
public class DeptController {

    @Autowired
    private SysDeptService deptService;

    /**
     * 查询所有部门
     *
     * @return 部门列表
     */
    @GetMapping("/list")
    public R<List<DeptRespVO>> list() {
        return R.ok(deptService.listDepts());
    }

    /**
     * 新增部门
     *
     * @param reqVO 部门信息
     * @return 操作结果
     */
    @PostMapping
    public R<Void> add(@RequestBody DeptSaveReqVO reqVO) {
        deptService.addDept(reqVO);
        return R.ok();
    }

    /**
     * 更新部门信息
     *
     * @param reqVO 部门信息
     * @return 操作结果
     */
    @PutMapping
    public R<Void> update(@RequestBody DeptSaveReqVO reqVO) {
        deptService.updateDept(reqVO);
        return R.ok();
    }

    /**
     * 删除部门（逻辑删除）
     *
     * @param id 部门ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        deptService.removeById(id);
        return R.ok();
    }
}
