package io.github.luckyqing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.luckyqing.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper 接口
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询所有用户（关联部门名称）
     *
     * @return 用户列表（含部门名称）
     */
    List<SysUser> selectAllWithDept();

    /**
     * 根据ID查询用户（关联部门名称）
     *
     * @param id 用户ID
     * @return 用户信息（含部门名称）
     */
    SysUser selectByIdWithDept(@Param("id") Long id);
}
