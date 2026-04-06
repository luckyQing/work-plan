package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.vo.demand.DemandRespVO;
import io.github.luckyqing.vo.demand.DemandSaveReqVO;
import io.github.luckyqing.service.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 需求管理接口
 * 提供需求的录入、查看、修改、删除功能
 */
@RestController
@RequestMapping("/api/demand")
public class DemandController {

    @Autowired
    private DemandService demandService;

    /**
     * 查询需求列表（含项目名称、创建人姓名）
     *
     * @return 需求列表
     */
    @GetMapping("/list")
    public R<List<DemandRespVO>> list() {
        return R.ok(demandService.listDemandsWithDetail());
    }

    /**
     * 根据ID查询需求详情
     *
     * @param id 需求ID
     * @return 需求信息
     */
    @GetMapping("/{id}")
    public R<DemandRespVO> getById(@PathVariable Long id) {
        return R.ok(demandService.getDemandById(id));
    }

    /**
     * 新增需求
     * 自动设置当前登录用户为创建人
     *
     * @param reqVO   需求信息
     * @param request HTTP请求（获取当前登录用户ID）
     * @return 操作结果
     */
    @PostMapping
    public R<Void> add(@Valid @RequestBody DemandSaveReqVO reqVO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        demandService.addDemand(reqVO, userId);
        return R.ok();
    }

    /**
     * 更新需求信息
     *
     * @param reqVO 需求信息
     * @return 操作结果
     */
    @PutMapping
    public R<Void> update(@Valid @RequestBody DemandSaveReqVO reqVO) {
        demandService.updateDemand(reqVO);
        return R.ok();
    }

    /**
     * 删除需求（逻辑删除）
     *
     * @param id 需求ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        demandService.removeById(id);
        return R.ok();
    }
}
