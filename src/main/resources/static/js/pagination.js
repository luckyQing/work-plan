/**
 * 公共前端分页工具
 * 用法：
 *   var pager = new Pagination({ containerId: 'pagination', pageSize: 15, onRender: function(pageData, allData) { ... } });
 *   pager.setData(allData);       // 设置全量数据并渲染第1页
 *   pager.filter(keyword, field); // 按字段模糊过滤
 *   pager.reset();                // 重置搜索
 */
function Pagination(opts) {
    this.containerId = opts.containerId || 'pagination';
    this.pageSize = opts.pageSize || 15;
    this.onRender = opts.onRender || function() {};
    this.allData = [];
    this.filtered = [];
    this.currentPage = 1;
}

Pagination.prototype.setData = function(data) {
    this.allData = data || [];
    this.filtered = this.allData;
    this.currentPage = 1;
    this.render();
};

Pagination.prototype.filter = function(keyword, field) {
    var kw = (keyword || '').trim().toLowerCase();
    if (!kw) {
        this.filtered = this.allData;
    } else {
        this.filtered = this.allData.filter(function(item) {
            var val = item[field];
            return val && String(val).toLowerCase().indexOf(kw) >= 0;
        });
    }
    this.currentPage = 1;
    this.render();
};

Pagination.prototype.reset = function() {
    this.filtered = this.allData;
    this.currentPage = 1;
    this.render();
};

Pagination.prototype.goPage = function(page) {
    var totalPages = Math.max(1, Math.ceil(this.filtered.length / this.pageSize));
    if (page < 1 || page > totalPages) return;
    this.currentPage = page;
    this.render();
};

Pagination.prototype.render = function() {
    var total = this.filtered.length;
    var totalPages = Math.max(1, Math.ceil(total / this.pageSize));
    if (this.currentPage > totalPages) this.currentPage = totalPages;
    var start = (this.currentPage - 1) * this.pageSize;
    var pageData = this.filtered.slice(start, start + this.pageSize);

    // 回调渲染表格
    this.onRender(pageData, this.filtered);

    // 渲染分页条
    var container = document.getElementById(this.containerId);
    if (!container) return;
    var self = this;
    var html = '<span style="font-size:13px;color:#999;margin-right:8px;">共 ' + total + ' 条</span>';
    html += '<span class="page-btn' + (this.currentPage <= 1 ? ' disabled' : '') + '" data-page="' + (this.currentPage - 1) + '">‹</span>';
    var startP = Math.max(1, this.currentPage - 3);
    var endP = Math.min(totalPages, startP + 6);
    if (endP - startP < 6) startP = Math.max(1, endP - 6);
    for (var i = startP; i <= endP; i++) {
        html += '<span class="page-btn' + (i === this.currentPage ? ' active' : '') + '" data-page="' + i + '">' + i + '</span>';
    }
    html += '<span class="page-btn' + (this.currentPage >= totalPages ? ' disabled' : '') + '" data-page="' + (this.currentPage + 1) + '">›</span>';
    container.innerHTML = html;

    // 绑定点击
    container.querySelectorAll('.page-btn').forEach(function(btn) {
        btn.addEventListener('click', function() {
            var p = parseInt(this.getAttribute('data-page'));
            self.goPage(p);
        });
    });
};
