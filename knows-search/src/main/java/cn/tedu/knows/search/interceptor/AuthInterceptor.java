package cn.tedu.knows.search.interceptor;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

// 别忘了编写@Component注解
@Component
public class AuthInterceptor implements HandlerInterceptor {
    // 解析Jwt需要Ribbon添加依赖
    @Resource
    private RestTemplate restTemplate;

    // 需要用户信息的控制器运行之前,先运行这个拦截器方法
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // 1.从请求中获得JWT
        String token=request.getParameter("accessToken");
        // 2.向auth模块的验证令牌的方法发起Ribbon请求
        String url="http://auth-service/oauth/check_token?token={1}";
        // Ribbon的返回值可以使用Map来接收,自动将json的key和value赋值到map中
        Map<String,Object> map=restTemplate
                .getForObject(url, Map.class,token);
        // 3.解析返回的用户信息(提取用户名\权限\角色)
        String username=map.get("user_name").toString();
        List<String> list=(List<String>)map.get("authorities");
        // 4.用户信息保存为UserDetails类型对象
        // Spring-Security框架规定了UserDetails类型对象为保存用户信息的对象
        // 我们必须遵守框架的要求,实例化这个对象并为相关属性赋值
        // UserDetails中用户权限要求时String类型数组,而我们现在是List类型
        // 将List<String>转换为String[]
        String[] auth=list.toArray(new String[0]);
        UserDetails details= User.builder()
                .username(username)
                .password("")
                .authorities(auth)
                .build();
        // 5.将UserDetails类型对象保存到Spring-Security容器中,以便控制器获取
        // 这个操作的过程是Spring-Security框架规定好的,不需要我们记忆
        // 需要时照搬即可
        PreAuthenticatedAuthenticationToken authenticationToken
                =new PreAuthenticatedAuthenticationToken(
                        details,details.getPassword(),
                AuthorityUtils.createAuthorityList(auth));
        // 关联当前请求,上面的信息保存在request对象中,以便控制器获取
        authenticationToken.setDetails(
                new WebAuthenticationDetails(request));
        //将用户详情对象保存在Spring-Security容器中
        SecurityContextHolder.getContext()
                .setAuthentication(authenticationToken);
        // 最后别忘了返回true
        return true;

    }
}






