/**
 * 公共布局脚本
 * 生成左侧导航栏，处理收缩/展开、高亮当前页、登出
 */
(function() {
    var currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null');
    var token = localStorage.getItem('token');
    if (!token) { location.href = 'login.html'; return; }

    var menus = [
        { href: 'dashboard.html',    icon: '🏠', label: '工作台' },
        { href: 'week-view.html',    icon: '📅', label: '周排期' },
        { href: 'demand-manage.html', icon: '📋', label: '需求管理' },
        { href: 'user-manage.html',  icon: '👥', label: '人员管理' },
        { href: 'config-manage.html', icon: '⚙️', label: '字典配置' }
    ];

    var currentPage = location.pathname.split('/').pop() || 'index.html';
    var collapsed = localStorage.getItem('sidebarCollapsed') === 'true';

    // 收缩/展开图标：展开时显示 ‹（向左，表示可收起），收起时显示 ›（向右，表示可展开）
    function toggleIcon(isCollapsed) { return isCollapsed ? '›' : '‹'; }

    var sidebar = document.createElement('div');
    sidebar.className = 'sidebar' + (collapsed ? ' collapsed' : '');
    sidebar.innerHTML =
        '<div class="sidebar-inner">' +
            '<div class="logo">' +
                '<span class="logo-icon">📊</span>' +
                '<span class="logo-text">任务排期管理</span>' +
            '</div>' +
            '<div class="nav-menu">' +
                menus.map(function(m) {
                    var active = currentPage === m.href ? ' active' : '';
                    return '<a class="nav-item' + active + '" href="' + m.href + '">' +
                        '<span class="nav-icon">' + m.icon + '</span>' +
                        '<span class="nav-label">' + m.label + '</span></a>';
                }).join('') +
            '</div>' +
            '<div class="user-area">' +
                '<span class="user-icon">👤</span>' +
                '<span class="user-name">' + (currentUser ? currentUser.realName : '') + '</span>' +
                '<button class="logout-btn" title="退出登录">退出</button>' +
            '</div>' +
        '</div>' +
        '<div class="toggle-btn" title="收缩/展开导航">' + toggleIcon(collapsed) + '</div>';

    document.body.insertBefore(sidebar, document.body.firstChild);

    sidebar.querySelector('.toggle-btn').addEventListener('click', function() {
        collapsed = !collapsed;
        sidebar.classList.toggle('collapsed');
        this.textContent = toggleIcon(collapsed);
        localStorage.setItem('sidebarCollapsed', collapsed);
        window.dispatchEvent(new Event('resize'));
    });

    sidebar.querySelector('.logout-btn').addEventListener('click', function() {
        fetch('/api/auth/logout', { method: 'POST', headers: { 'Authorization': 'Bearer ' + token } }).catch(function(){});
        localStorage.removeItem('token');
        localStorage.removeItem('currentUser');
        location.href = 'login.html';
    });
})();
