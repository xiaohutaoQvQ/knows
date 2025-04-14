package cn.tedu.knows.faq.security;

import cn.tedu.knows.faq.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sun.net.www.protocol.http.AuthenticationInfo;

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
                        "/v2/questions",         //发布问题
                        "/v2/questions/my",      //学生首页
                        "/v2/questions/teacher", //讲师首页
                        "/v2/answers",           //新增回答
                        "/v2/answers/*/solved",  //采纳回答
                        "/v2/comments",          //新增评论
                        "/v2/comments/*/delete", //删除评论
                        "/v2/comments/*/update"  //修改评论
                );
    }
}
