package io.github.luckyqing.config;

import io.github.luckyqing.shiro.JwtFilter;
import io.github.luckyqing.shiro.JwtRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置（无状态 JWT 模式，禁用 Session）
 *
 * 注意：不使用 DefaultAdvisorAutoProxyCreator，避免与 Spring Boot 内置代理冲突。
 * 使用 AuthorizationAttributeSourceAdvisor + @EnableAspectJAutoProxy(proxyTargetClass=true)
 * 来支持 @RequiresRoles/@RequiresPermissions 注解，且强制走 CGLIB 代理。
 */
@Configuration
public class ShiroConfig {

    @Bean
    public JwtRealm jwtRealm() {
        return new JwtRealm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(jwtRealm());
        // 禁用 Session，实现无状态
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    /**
     * 开启 Shiro 注解（@RequiresRoles/@RequiresPermissions）支持
     * 使用 AuthorizationAttributeSourceAdvisor，由 Spring AOP 统一代理（CGLIB）
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        // 注册自定义 JWT 过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new JwtFilter());
        factoryBean.setFilters(filters);

        // 过滤链：放行登录接口和静态资源，其余需要 JWT 认证
        LinkedHashMap<String, String> filterChain = new LinkedHashMap<>();
        filterChain.put("/api/auth/login", "anon");
        filterChain.put("/api/auth/logout", "anon");
        filterChain.put("/*.html", "anon");
        filterChain.put("/css/**", "anon");
        filterChain.put("/js/**", "anon");
        filterChain.put("/api/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(filterChain);
        return factoryBean;
    }
}
