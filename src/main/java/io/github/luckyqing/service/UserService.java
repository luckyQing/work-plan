package io.github.luckyqing.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.common.R;
import io.github.luckyqing.entity.User;
import io.github.luckyqing.mapper.UserMapper;
import io.github.luckyqing.vo.user.UserListReqVO;
import io.github.luckyqing.vo.user.UserRespVO;
import io.github.luckyqing.vo.user.UserSaveReqVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理服务
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public List<UserRespVO> listUsers(UserListReqVO reqVO) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        if (reqVO.getDept() != null && !reqVO.getDept().isEmpty()) {
            qw.eq(User::getDept, reqVO.getDept());
        }
        return list(qw).stream().map(this::toRespVO).collect(Collectors.toList());
    }

    public UserRespVO getUserById(Long id) {
        User user = getById(id);
        return user != null ? toRespVO(user) : null;
    }

    public R<Void> addUser(UserSaveReqVO reqVO) {
        User exist = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, reqVO.getUsername()));
        if (exist != null) return R.fail("用户名已存在");
        User user = toEntity(reqVO);
        user.setPassword(DigestUtil.md5Hex(reqVO.getPassword()));
        save(user);
        return R.ok();
    }

    public R<Void> updateUser(UserSaveReqVO reqVO) {
        User user = toEntity(reqVO);
        if (reqVO.getPassword() != null && !reqVO.getPassword().isEmpty()) {
            user.setPassword(DigestUtil.md5Hex(reqVO.getPassword()));
        } else {
            user.setPassword(null);
        }
        updateById(user);
        return R.ok();
    }

    /** 按部门查询用户（供排期服务调用） */
    public List<User> listByDept(String dept) {
        return list(new LambdaQueryWrapper<User>()
                .eq(dept != null && !dept.isEmpty(), User::getDept, dept));
    }

    private UserRespVO toRespVO(User user) {
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

    private User toEntity(UserSaveReqVO reqVO) {
        User user = new User();
        user.setId(reqVO.getId());
        user.setUsername(reqVO.getUsername());
        user.setRealName(reqVO.getRealName());
        user.setDept(reqVO.getDept());
        user.setRole(reqVO.getRole());
        user.setStatus(reqVO.getStatus());
        return user;
    }
}
