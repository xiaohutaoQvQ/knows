package cn.tedu.knows.search.security;


import cn.tedu.knows.search.interceptor.AuthInterceptor;
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

    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        "/v3/questions"  //搜索问题
                );
    }
}
