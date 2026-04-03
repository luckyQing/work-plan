package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.vo.user.UserListReqVO;
import io.github.luckyqing.vo.user.UserRespVO;
import io.github.luckyqing.vo.user.UserSaveReqVO;
import io.github.luckyqing.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理接口
 * 提供用户的增删改查功能
 */
@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    /**
     * 查询用户列表
     * 可选按部门筛选，不传 deptId 则查询全部
     *
     * @param reqVO 查询参数
     * @return 用户列表
     */
    @GetMapping("/list")
    public R<List<UserRespVO>> list(UserListReqVO reqVO) {
        return R.ok(userService.listUsers(reqVO));
    }

    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public R<UserRespVO> getById(@PathVariable Long id) {
        return R.ok(userService.getUserById(id));
    }

    /**
     * 新增用户
     *
     * @param reqVO 用户信息
     * @return 操作结果
     */
    @PostMapping
    public R<Void> add(@RequestBody UserSaveReqVO reqVO) {
        return userService.addUser(reqVO);
    }

    /**
     * 更新用户信息
     *
     * @param reqVO 用户信息
     * @return 操作结果
     */
    @PutMapping
    public R<Void> update(@RequestBody UserSaveReqVO reqVO) {
        return userService.updateUser(reqVO);
    }

    /**
     * 删除用户（逻辑删除）
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return R.ok();
    }
}
