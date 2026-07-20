package io.github.luckyqing.resposity;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.common.R;
import io.github.luckyqing.convert.EntityConvert;
import io.github.luckyqing.entity.DemandEntity;
import io.github.luckyqing.entity.TaskEntity;
import io.github.luckyqing.entity.dataobject.DemandDO;
import io.github.luckyqing.mapper.DemandMapper;
import io.github.luckyqing.vo.demand.DemandRespVO;
import io.github.luckyqing.vo.demand.DemandSaveReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 需求表 服务实现类
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Service
public class DemandResposity extends ServiceImpl<DemandMapper, DemandEntity> {

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
        DemandEntity demand = getById(id);
        if (demand == null) {
            return null;
        }
        DemandDO d = EntityConvert.INSTANCE.toDemandDO(demand);
        return toRespVO(d);
    }

    /**
     * 新增需求
     *
     * @param reqVO     需求信息
     * @param creatorId 创建人ID（当前登录用户）
     */
    public void addDemand(DemandSaveReqVO reqVO, Long creatorId) {
        DemandEntity demand = toEntity(reqVO);
        demand.setCreatorId(creatorId);
        save(demand);
    }

    /**
     * 更新需求信息
     *
     * @param reqVO 需求信息
     */
    public void updateDemand(DemandSaveReqVO reqVO) {
        DemandEntity demand = toEntity(reqVO);
        updateById(demand);
    }

    /**
     * Entity 转 RespVO
     */
    private DemandRespVO toRespVO(DemandDO demand) {
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
    private DemandEntity toEntity(DemandSaveReqVO reqVO) {
        DemandEntity demand = new DemandEntity();
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
    private TaskResposity taskService;

    /** 需求状态值常量（与字典 demand_status 的 config_value 对应） */
    private static final int STATUS_TODO = 10;
    private static final int STATUS_DOING = 20;
    private static final int STATUS_DONE = 30;

    /** 开始需求 */
    public R<Void> startDemand(Long id) {
        DemandEntity demand = getById(id);
        if (demand == null) {
            return R.fail("需求不存在");
        }
        if (demand.getStatus() != null && demand.getStatus() != STATUS_TODO) {
            return R.fail("只有待开始的需求才能开始");
        }
        DemandEntity update = new DemandEntity();
        update.setId(id);
        update.setStatus(STATUS_DOING);
        updateById(update);
        return R.ok();
    }

    /** 完成需求（需所有子任务已完成） */
    public R<Void> completeDemand(Long id) {
        DemandEntity demand = getById(id);
        if (demand == null) {
            return R.fail("需求不存在");
        }
        if (demand.getStatus() == null || demand.getStatus() != STATUS_DOING) {
            return R.fail("只有进行中的需求才能完成");
        }
        // 检查该需求下是否有未完成的任务
        long unfinished = taskService.count(new LambdaQueryWrapper<TaskEntity>()
                .eq(TaskEntity::getDemandId, id)
                .ne(TaskEntity::getStatus, 2)); // status=2 表示已完成
        if (unfinished > 0) {
            return R.fail("该需求下还有 " + unfinished + " 个未完成的任务，无法完成");
        }
        DemandEntity update = new DemandEntity();
        update.setId(id);
        update.setStatus(STATUS_DONE);
        updateById(update);
        return R.ok();
    }

    /** 查询进行中的需求（周排期录入任务用） */
    public List<DemandRespVO> listActiveDemands() {
        List<DemandEntity> list = list(new LambdaQueryWrapper<DemandEntity>()
                .eq(DemandEntity::getStatus, STATUS_DOING)
                .orderByDesc(DemandEntity::getCreateTime));
        return EntityConvert.INSTANCE.toDemandDOList(list).stream()
                .map(this::toRespVO).collect(Collectors.toList());
    }
}
