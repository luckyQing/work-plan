package io.github.luckyqing.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.SysDept;
import io.github.luckyqing.mapper.SysDeptMapper;
import io.github.luckyqing.vo.dept.DeptRespVO;
import io.github.luckyqing.vo.dept.DeptSaveReqVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理服务
 */
@Service
public class SysDeptService extends ServiceImpl<SysDeptMapper, SysDept> {

    /**
     * 查询所有部门
     *
     * @return 部门RespVO列表
     */
    public List<DeptRespVO> listDepts() {
        return list().stream().map(this::toRespVO).collect(Collectors.toList());
    }

    /**
     * 新增部门
     *
     * @param reqVO 部门信息
     */
    public void addDept(DeptSaveReqVO reqVO) {
        SysDept dept = toEntity(reqVO);
        save(dept);
    }

    /**
     * 更新部门信息
     *
     * @param reqVO 部门信息
     */
    public void updateDept(DeptSaveReqVO reqVO) {
        SysDept dept = toEntity(reqVO);
        updateById(dept);
    }

    /**
     * Entity 转 RespVO
     */
    private DeptRespVO toRespVO(SysDept dept) {
        DeptRespVO vo = new DeptRespVO();
        vo.setId(dept.getId());
        vo.setDeptName(dept.getDeptName());
        vo.setCreateTime(dept.getCreateTime());
        return vo;
    }

    /**
     * ReqVO 转 Entity
     */
    private SysDept toEntity(DeptSaveReqVO reqVO) {
        SysDept dept = new SysDept();
        dept.setId(reqVO.getId());
        dept.setDeptName(reqVO.getDeptName());
        return dept;
    }
}
