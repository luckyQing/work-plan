package io.github.luckyqing.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.common.R;
import io.github.luckyqing.entity.SysUser;
import io.github.luckyqing.mapper.SysUserMapper;
import io.github.luckyqing.vo.user.UserListReqVO;
import io.github.luckyqing.vo.user.UserRespVO;
import io.github.luckyqing.vo.user.UserSaveReqVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理服务
 * 提供用户的增删改查功能
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    /**
     * 查询用户列表（含部门名称）
     * 可选按部门筛选
     *
     * @param reqVO 查询参数
     * @return 用户RespVO列表
     */
    public List<UserRespVO> listUsers(UserListReqVO reqVO) {
        List<SysUser> users;
        if (reqVO.getDeptId() != null) {
            users = list(new LambdaQueryWrapper<SysUser>().eq(SysUser::getDeptId, reqVO.getDeptId()));
        } else {
            users = baseMapper.selectAllWithDept();
        }
        return users.stream().map(this::toRespVO).collect(Collectors.toList());
    }

    /**
     * 根据ID查询用户（含部门名称）
     *
     * @param id 用户ID
     * @return 用户RespVO
     */
    public UserRespVO getUserById(Long id) {
        SysUser user = baseMapper.selectByIdWithDept(id);
        return user != null ? toRespVO(user) : null;
    }

    /**
     * 新增用户
     * 校验用户名唯一性，密码进行 MD5 加密
     *
     * @param reqVO 用户信息
     * @return 操作结果
     */
    public R<Void> addUser(UserSaveReqVO reqVO) {
        // 校验用户名是否已存在
        SysUser exist = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, reqVO.getUsername()));
        if (exist != null) {
            return R.fail("用户名已存在");
        }
        // ReqVO 转 Entity
        SysUser user = toEntity(reqVO);
        // 密码 MD5 加密
        user.setPassword(DigestUtil.md5Hex(reqVO.getPassword()));
        save(user);
        return R.ok();
    }

    /**
     * 更新用户信息
     * 如果传了密码则重新加密，否则不更新密码字段
     *
     * @param reqVO 用户信息
     * @return 操作结果
     */
    public R<Void> updateUser(UserSaveReqVO reqVO) {
        SysUser user = toEntity(reqVO);
        if (reqVO.getPassword() != null && !reqVO.getPassword().isEmpty()) {
            user.setPassword(DigestUtil.md5Hex(reqVO.getPassword()));
        } else {
            // 不更新密码
            user.setPassword(null);
        }
        updateById(user);
        return R.ok();
    }

    /**
     * 根据部门ID查询用户列表（内部使用，供排期服务调用）
     *
     * @param deptId 部门ID
     * @return 用户Entity列表
     */
    public List<SysUser> listByDept(Long deptId) {
        return list(new LambdaQueryWrapper<SysUser>().eq(deptId != null, SysUser::getDeptId, deptId));
    }

    /**
     * Entity 转 RespVO
     */
    private UserRespVO toRespVO(SysUser user) {
        UserRespVO vo = new UserRespVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setDeptId(user.getDeptId());
        vo.setDeptName(user.getDeptName());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }

    /**
     * ReqVO 转 Entity
     */
    private SysUser toEntity(UserSaveReqVO reqVO) {
        SysUser user = new SysUser();
        user.setId(reqVO.getId());
        user.setUsername(reqVO.getUsername());
        user.setRealName(reqVO.getRealName());
        user.setDeptId(reqVO.getDeptId());
        user.setRole(reqVO.getRole());
        user.setStatus(reqVO.getStatus());
        return user;
    }
}
