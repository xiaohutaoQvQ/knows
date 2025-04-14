package cn.tedu.knows.sys.security;

import cn.tedu.knows.sys.interceptor.AuthInterceptor;
import cn.tedu.knows.sys.interceptor.DemoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

// 配置Spring的类都需要添加下面注解
// SpringMvc也是Spring衍生出的框架,也要添加这个注解
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 重写设置跨域的方法
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置当前项目所有请求都允许跨域
        registry.addMapping("/**") // 匹配访问任何资源路径
                .allowedOrigins("*")         // 允许任何访问源跨域
                .allowedMethods("*")         // 允许任何方法(get\post)
                .allowedHeaders("*");        // 允许任何请求头
    }

    // 配置拦截器,先从Spring容器中获得它
    @Resource
    private DemoInterceptor demoInterceptor;

    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 先指定配置哪个拦截器对象
        registry.addInterceptor(demoInterceptor)
                // 设置拦截器生效路径
                .addPathPatterns("/v1/auth/demo");

        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/v1/home","/v1/users/me");
    }
}
