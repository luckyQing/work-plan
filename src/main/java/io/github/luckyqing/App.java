package io.github.luckyqing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 任务排期管理系统启动类
 */
@SpringBootApplication
@MapperScan("io.github.luckyqing.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true)  // 强制 CGLIB 代理，解决 Shiro 注解与 ServiceImpl 的代理冲突
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
