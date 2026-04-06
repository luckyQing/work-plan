package io.github.luckyqing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.common.R;
import io.github.luckyqing.entity.Demand;
import io.github.luckyqing.entity.Task;
import io.github.luckyqing.mapper.DemandMapper;
import io.github.luckyqing.vo.demand.DemandRespVO;
import io.github.luckyqing.vo.demand.DemandSaveReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 需求管理服务
 * 提供需求的增删改查功能
 */
@Service
public class DemandService extends ServiceImpl<DemandMapper, Demand> {

    /**
     * 查询所有需求（含项目名称、创建人姓名）
     *
     * @return 需求RespVO列表
     */
    public List<DemandRespVO> listDemandsWithDetail() {
        return baseMapper.selectAllWithDetail().stream().map(this::toRespVO).collect(Collectors.toList());
    }

    /**
     * 根据ID查询需求详情
     *
     * @param id 需求ID
     * @return 需求RespVO
     */
    public DemandRespVO getDemandById(Long id) {
        Demand demand = getById(id);
        return demand != null ? toRespVO(demand) : null;
    }

    /**
     * 新增需求
     *
     * @param reqVO     需求信息
     * @param creatorId 创建人ID（当前登录用户）
     */
    public void addDemand(DemandSaveReqVO reqVO, Long creatorId) {
        Demand demand = toEntity(reqVO);
        demand.setCreatorId(creatorId);
        save(demand);
    }

    /**
     * 更新需求信息
     *
     * @param reqVO 需求信息
     */
    public void updateDemand(DemandSaveReqVO reqVO) {
        Demand demand = toEntity(reqVO);
        updateById(demand);
    }

    /**
     * Entity 转 RespVO
     */
    private DemandRespVO toRespVO(Demand demand) {
        DemandRespVO vo = new DemandRespVO();
        vo.setId(demand.getId());
        vo.setDemandName(demand.getDemandName());
        vo.setDemandType(demand.getDemandType());
        vo.setProjectId(demand.getProjectId());
        vo.setProjectName(demand.getProjectName());
        vo.setDescription(demand.getDescription());
        vo.setStatus(demand.getStatus());
        vo.setPriority(demand.getPriority());
        vo.setCreatorId(demand.getCreatorId());
        vo.setCreatorName(demand.getCreatorName());
        vo.setStartDate(demand.getStartDate());
        vo.setEndDate(demand.getEndDate());
        vo.setTotalHours(demand.getTotalHours());
        vo.setProductMembers(demand.getProductMembers());
        vo.setTestMembers(demand.getTestMembers());
        vo.setDevMembers(demand.getDevMembers());
        vo.setCreateTime(demand.getCreateTime());
        return vo;
    }

    /**
     * ReqVO 转 Entity
     */
    private Demand toEntity(DemandSaveReqVO reqVO) {
        Demand demand = new Demand();
        demand.setId(reqVO.getId());
        demand.setDemandName(reqVO.getDemandName());
        demand.setDemandType(reqVO.getDemandType());
        demand.setProjectId(reqVO.getProjectId());
        demand.setDescription(reqVO.getDescription());
        demand.setStatus(reqVO.getStatus());
        demand.setPriority(reqVO.getPriority());
        if (reqVO.getStartDate() != null) {
            demand.setStartDate(LocalDate.parse(reqVO.getStartDate()));
        }
        if (reqVO.getEndDate() != null) {
            demand.setEndDate(LocalDate.parse(reqVO.getEndDate()));
        }
        demand.setTotalHours(reqVO.getTotalHours());
        demand.setProductMembers(reqVO.getProductMembers());
        demand.setTestMembers(reqVO.getTestMembers());
        demand.setDevMembers(reqVO.getDevMembers());
        return demand;
    }

    @Autowired
    private TaskService taskService;

    /** 需求状态值常量（与字典 demand_status 的 config_value 对应） */
    private static final int STATUS_TODO = 10;
    private static final int STATUS_DOING = 20;
    private static final int STATUS_DONE = 30;

    /** 开始需求 */
    public R<Void> startDemand(Long id) {
        Demand demand = getById(id);
        if (demand == null) return R.fail("需求不存在");
        if (demand.getStatus() != null && demand.getStatus() != STATUS_TODO) {
            return R.fail("只有待开始的需求才能开始");
        }
        Demand update = new Demand();
        update.setId(id);
        update.setStatus(STATUS_DOING);
        updateById(update);
        return R.ok();
    }

    /** 完成需求（需所有子任务已完成） */
    public R<Void> completeDemand(Long id) {
        Demand demand = getById(id);
        if (demand == null) return R.fail("需求不存在");
        if (demand.getStatus() == null || demand.getStatus() != STATUS_DOING) {
            return R.fail("只有进行中的需求才能完成");
        }
        // 检查该需求下是否有未完成的任务
        long unfinished = taskService.count(new LambdaQueryWrapper<Task>()
                .eq(Task::getDemandId, id)
                .ne(Task::getStatus, 2)); // status=2 表示已完成
        if (unfinished > 0) {
            return R.fail("该需求下还有 " + unfinished + " 个未完成的任务，无法完成");
        }
        Demand update = new Demand();
        update.setId(id);
        update.setStatus(STATUS_DONE);
        updateById(update);
        return R.ok();
    }

    /** 查询进行中的需求（周排期录入任务用） */
    public List<DemandRespVO> listActiveDemands() {
        List<Demand> list = list(new LambdaQueryWrapper<Demand>()
                .eq(Demand::getStatus, STATUS_DOING)
                .orderByDesc(Demand::getCreateTime));
        return list.stream().map(this::toRespVO).collect(Collectors.toList());
    }
}
