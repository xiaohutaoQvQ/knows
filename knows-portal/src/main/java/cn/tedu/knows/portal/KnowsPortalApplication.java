package cn.tedu.knows.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// SpringBoot项目也是在Spring框架基础上创建的
// 它有Spring容器的概念,也有组件扫描的概念
// 实际上当前SpringBoot启动类就类似于我们创建的XXXConfig类
// 当前SpringBoot启动类会默认扫描当前类所在的包:cn.tedu.knows.portal
@SpringBootApplication
// 下面的注解是Mybatis框架包含的注解,指定一个包,包中的所有接口
// 都会被视为添加了@Mapper注解
@MapperScan("cn.tedu.knows.portal.mapper")
public class KnowsPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowsPortalApplication.class, args);
    }

}
