package cn.tedu.knows.sys.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 拦截器对象也要保存到Spring容器统一管理
@Component
public class DemoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 这个方法会在控制器运行之前运行
        System.out.println("preHandle运行");
        // 该方法返回boolean类型
        // 返回true表示允许当前请求继续访问控制器方法
        // 返回false表示阻止当前请求继续访问控制器方法
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在控制器方法运行之后执行
        System.out.println("postHandle运行");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在页面显示结果之前运行
        System.out.println("afterCompletion运行");
    }
}
