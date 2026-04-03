package io.github.luckyqing.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.Demand;
import io.github.luckyqing.mapper.DemandMapper;
import io.github.luckyqing.vo.demand.DemandRespVO;
import io.github.luckyqing.vo.demand.DemandSaveReqVO;
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
        return demand;
    }
}
