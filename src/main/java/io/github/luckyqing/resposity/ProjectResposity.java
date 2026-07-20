package io.github.luckyqing.resposity;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.ProjectEntity;
import io.github.luckyqing.mapper.ProjectMapper;
import io.github.luckyqing.vo.project.ProjectRespVO;
import io.github.luckyqing.vo.project.ProjectSaveReqVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Service
public class ProjectResposity extends ServiceImpl<ProjectMapper, ProjectEntity> {

    /**
     * 查询所有项目
     *
     * @return 项目RespVO列表
     */
    public List<ProjectRespVO> listProjects() {
        return list().stream().map(this::toRespVO).collect(Collectors.toList());
    }

    /**
     * 新增项目
     *
     * @param reqVO 项目信息
     */
    public void addProject(ProjectSaveReqVO reqVO) {
        ProjectEntity project = toEntity(reqVO);
        save(project);
    }

    /**
     * 更新项目信息
     *
     * @param reqVO 项目信息
     */
    public void updateProject(ProjectSaveReqVO reqVO) {
        ProjectEntity project = toEntity(reqVO);
        updateById(project);
    }

    /**
     * Entity 转 RespVO
     */
    private ProjectRespVO toRespVO(ProjectEntity project) {
        ProjectRespVO vo = new ProjectRespVO();
        vo.setId(project.getId());
        vo.setProjectName(project.getProjectName());
        vo.setStatus(project.getStatus());
        vo.setCreateTime(project.getCreateTime());
        return vo;
    }

    /**
     * ReqVO 转 Entity
     */
    private ProjectEntity toEntity(ProjectSaveReqVO reqVO) {
        ProjectEntity project = new ProjectEntity();
        project.setId(reqVO.getId());
        project.setProjectName(reqVO.getProjectName());
        project.setStatus(reqVO.getStatus());
        return project;
    }
}
