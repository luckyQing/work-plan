package io.github.luckyqing.resposity;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.common.R;
import io.github.luckyqing.entity.UserEntity;
import io.github.luckyqing.mapper.UserMapper;
import io.github.luckyqing.vo.user.UserListReqVO;
import io.github.luckyqing.vo.user.UserRespVO;
import io.github.luckyqing.vo.user.UserSaveReqVO;
import io.github.luckyqing.vo.user.ChangePasswordReqVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Service
public class UserResposity extends ServiceImpl<UserMapper, UserEntity> {

    public List<UserRespVO> listUsers(UserListReqVO reqVO) {
        LambdaQueryWrapper<UserEntity> qw = new LambdaQueryWrapper<>();
        if (reqVO.getDept() != null && !reqVO.getDept().isEmpty()) {
            qw.eq(UserEntity::getDept, reqVO.getDept());
        }
        return list(qw).stream().map(this::toRespVO).collect(Collectors.toList());
    }

    public UserRespVO getUserById(Long id) {
        UserEntity user = getById(id);
        return user != null ? toRespVO(user) : null;
    }

    public R<Void> addUser(UserSaveReqVO reqVO) {
        UserEntity exist = getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, reqVO.getUsername()));
        if (exist != null) return R.fail("用户名已存在");
        UserEntity user = toEntity(reqVO);
        user.setPassword(DigestUtil.md5Hex(reqVO.getPassword()));
        save(user);
        return R.ok();
    }

    public R<Void> updateUser(UserSaveReqVO reqVO) {
        UserEntity user = toEntity(reqVO);
        if (reqVO.getPassword() != null && !reqVO.getPassword().isEmpty()) {
            user.setPassword(DigestUtil.md5Hex(reqVO.getPassword()));
        } else {
            user.setPassword(null);
        }
        updateById(user);
        return R.ok();
    }

    /** 按部门查询用户（供排期服务调用） */
    public List<UserEntity> listByDept(String dept) {
        return list(new LambdaQueryWrapper<UserEntity>()
                .eq(dept != null && !dept.isEmpty(), UserEntity::getDept, dept));
    }

    /**
     * 修改个人密码
     */
    public R<Void> changePassword(Long userId, ChangePasswordReqVO reqVO) {
        if (!reqVO.getNewPassword().equals(reqVO.getConfirmPassword())) {
            return R.fail("两次输入的新密码不一致");
        }
        UserEntity user = getById(userId);
        if (user == null) return R.fail("用户不存在");
        if (!DigestUtil.md5Hex(reqVO.getOldPassword()).equals(user.getPassword())) {
            return R.fail("原密码错误");
        }
        UserEntity update = new UserEntity();
        update.setId(userId);
        update.setPassword(DigestUtil.md5Hex(reqVO.getNewPassword()));
        updateById(update);
        return R.ok();
    }

    private UserRespVO toRespVO(UserEntity user) {
        UserRespVO vo = new UserRespVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setDept(user.getDept());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }

    private UserEntity toEntity(UserSaveReqVO reqVO) {
        UserEntity user = new UserEntity();
        user.setId(reqVO.getId());
        user.setUsername(reqVO.getUsername());
        user.setRealName(reqVO.getRealName());
        user.setDept(reqVO.getDept());
        user.setRole(reqVO.getRole());
        user.setStatus(reqVO.getStatus());
        return user;
    }
}
