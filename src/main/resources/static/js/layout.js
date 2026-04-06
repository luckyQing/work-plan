/**
 * 公共布局脚本
 * 左侧导航根据用户菜单权限动态渲染
 */
(function() {
    var currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null');
    var token = localStorage.getItem('token');
    if (!token) { location.href = 'login.html'; return; }

    // 完整菜单定义（前端维护顺序和图标）
    var ALL_MENUS = [
        { path: 'dashboard.html',        icon: '🏠', label: '工作台' },
        { path: 'week-view.html',        icon: '📅', label: '周排期' },
        { path: 'demand-manage.html',    icon: '📋', label: '需求管理' },
        { path: 'user-manage.html',      icon: '👥', label: '人员管理' },
        { path: 'role-manage.html',      icon: '🔑', label: '角色管理' },
        { path: 'permission-manage.html', icon: '🛡️', label: '权限管理' },
        { path: 'config-manage.html',    icon: '⚙️', label: '字典配置' }
    ];

    var currentPage = location.pathname.split('/').pop() || 'index.html';
    var collapsed = localStorage.getItem('sidebarCollapsed') === 'true';
    function toggleIcon(c) { return c ? '›' : '‹'; }

    var sidebar = document.createElement('div');
    sidebar.className = 'sidebar' + (collapsed ? ' collapsed' : '');
    sidebar.innerHTML =
        '<div class="sidebar-inner">' +
            '<div class="logo"><span class="logo-icon">📊</span><span class="logo-text">任务排期管理</span></div>' +
            '<div class="nav-menu" id="sidebarNavMenu"></div>' +
            '<div class="user-area">' +
                '<span class="user-icon">👤</span>' +
                '<span class="user-name user-name-btn" title="修改密码">' + (currentUser ? currentUser.realName : '') + '</span>' +
                '<button class="logout-btn" title="退出登录">退出</button>' +
            '</div>' +
        '</div>' +
        '<div class="toggle-btn" title="收缩/展开导航">' + toggleIcon(collapsed) + '</div>';
    document.body.insertBefore(sidebar, document.body.firstChild);

    function renderMenus(allowedPaths) {
        var navMenu = document.getElementById('sidebarNavMenu');
        // 根据后端返回的菜单路径过滤，只显示有权限的菜单
        var visibleMenus = ALL_MENUS.filter(function(m) {
            return allowedPaths.indexOf(m.path) >= 0;
        });
        if (visibleMenus.length === 0) {
            navMenu.innerHTML = '<div style="color:rgba(255,255,255,0.3);font-size:13px;padding:16px;text-align:center;">暂无菜单权限</div>';
            return;
        }
        navMenu.innerHTML = visibleMenus.map(function(m) {
            var active = currentPage === m.path ? ' active' : '';
            return '<a class="nav-item' + active + '" href="' + m.path + '">' +
                '<span class="nav-icon">' + m.icon + '</span>' +
                '<span class="nav-label">' + m.label + '</span></a>';
        }).join('');
    }

    // 异步加载用户菜单权限，提取 permPath 列表做匹配
    fetch('/api/permission/menus', {
        headers: { 'Authorization': 'Bearer ' + token }
    }).then(function(r) { return r.json(); }).then(function(res) {
        if (res.code === 200 && res.data) {
            var paths = res.data.map(function(m) { return m.permPath; }).filter(Boolean);
            renderMenus(paths);
        } else {
            // 接口异常，降级显示基础菜单
            renderMenus(['dashboard.html', 'week-view.html']);
        }
    }).catch(function() {
        renderMenus(['dashboard.html', 'week-view.html']);
    });

    // 收缩/展开
    sidebar.querySelector('.toggle-btn').addEventListener('click', function() {
        collapsed = !collapsed;
        sidebar.classList.toggle('collapsed');
        this.textContent = toggleIcon(collapsed);
        localStorage.setItem('sidebarCollapsed', collapsed);
        window.dispatchEvent(new Event('resize'));
    });

    // 登出
    sidebar.querySelector('.logout-btn').addEventListener('click', function() {
        fetch('/api/auth/logout', { method: 'POST', headers: { 'Authorization': 'Bearer ' + token } }).catch(function(){});
        localStorage.removeItem('token');
        localStorage.removeItem('currentUser');
        location.href = 'login.html';
    });

    // 修改密码
    sidebar.querySelector('.user-name-btn').addEventListener('click', function() { showChangePwdModal(); });

    // ===== 修改密码弹窗 =====
    var modal = document.createElement('div');
    modal.id = 'changePwdModal';
    modal.className = 'layout-modal-overlay';
    modal.innerHTML =
        '<div class="layout-modal">' +
            '<div class="layout-modal-header"><span>修改密码</span><span class="layout-modal-close" onclick="hideChangePwdModal()">×</span></div>' +
            '<div class="layout-modal-body">' +
                '<div class="layout-form-group"><label>原密码</label><input type="password" id="cpOldPwd" placeholder="请输入原密码"></div>' +
                '<div class="layout-form-group"><label>新密码</label><input type="password" id="cpNewPwd" placeholder="至少6位"></div>' +
                '<div class="layout-form-group"><label>确认新密码</label><input type="password" id="cpConfirmPwd" placeholder="再次输入新密码"></div>' +
                '<div id="cpErrMsg" style="color:#ff4d4f;font-size:13px;min-height:18px;margin-top:4px;"></div>' +
            '</div>' +
            '<div class="layout-modal-footer">' +
                '<button class="layout-btn" onclick="hideChangePwdModal()">取消</button>' +
                '<button class="layout-btn layout-btn-primary" onclick="submitChangePwd()">确认修改</button>' +
            '</div>' +
        '</div>';
    document.body.appendChild(modal);

    window.showChangePwdModal = function() {
        document.getElementById('cpOldPwd').value = '';
        document.getElementById('cpNewPwd').value = '';
        document.getElementById('cpConfirmPwd').value = '';
        document.getElementById('cpErrMsg').textContent = '';
        document.getElementById('changePwdModal').classList.add('active');
    };
    window.hideChangePwdModal = function() {
        document.getElementById('changePwdModal').classList.remove('active');
    };
    window.submitChangePwd = function() {
        var oldPwd = document.getElementById('cpOldPwd').value;
        var newPwd = document.getElementById('cpNewPwd').value;
        var confirmPwd = document.getElementById('cpConfirmPwd').value;
        var errEl = document.getElementById('cpErrMsg');
        if (!oldPwd) { errEl.textContent = '请输入原密码'; return; }
        if (!newPwd || newPwd.length < 6) { errEl.textContent = '新密码长度不能少于6位'; return; }
        if (newPwd !== confirmPwd) { errEl.textContent = '两次输入的新密码不一致'; return; }
        errEl.textContent = '';
        fetch('/api/user/changePassword', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body: JSON.stringify({ oldPassword: oldPwd, newPassword: newPwd, confirmPassword: confirmPwd })
        }).then(function(r) { return r.json(); }).then(function(res) {
            if (res.code === 200) {
                hideChangePwdModal();
                alert('密码修改成功，请重新登录');
                localStorage.removeItem('token');
                localStorage.removeItem('currentUser');
                location.href = 'login.html';
            } else {
                errEl.textContent = res.msg || '修改失败';
            }
        }).catch(function() { errEl.textContent = '网络错误，请重试'; });
    };
})();
