package cn.tedu.knows.auth.service;

import cn.tedu.knows.commons.exception.ServiceException;
import cn.tedu.knows.commons.model.Permission;
import cn.tedu.knows.commons.model.Role;
import cn.tedu.knows.commons.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

// 当前登录配置类需要保存到Spring容器
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根据用户名获得用户对象
        String url="http://sys-service/v1/auth/user?username={1}";
        User user=restTemplate
                .getForObject(url , User.class , username);
        // 2.判断查询出来的用户是否存在
        if(user==null){
            // 如果用户对象为空,抛出异常表示登录失败
            //throw new ServiceException("用户名密码错误!");
            throw new UsernameNotFoundException("用户名密码错误!");
        }
        // 3.根据用户id查询用户所有权限
        url="http://sys-service/v1/auth/permissions?id={1}";
        // Ribbon请求的控制器方法返回值为List时
        // 要使用该List泛型类型的数组来接收
        Permission[] permissions=restTemplate
                .getForObject(url , Permission[].class , user.getId());
        // 4.根据用户id查询用户所有角色
        url="http://sys-service/v1/auth/roles?id={1}";
        Role[] roles=restTemplate
                .getForObject(url , Role[].class , user.getId());
        // 5.将权限和角色保存在auth数组中
        String[] auth=new String[permissions.length+roles.length];
        // 分别遍历权限和角色数组,将权限和角色的名称保存在auth数组中
        int i=0;
        for(Permission p: permissions){
            auth[i++]=p.getName();
        }
        for(Role r: roles){
            auth[i++]=r.getName();
        }
        // 6.创建UserDetails对象
        UserDetails details= org.springframework.security
                .core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(auth)
                .accountLocked(user.getLocked()==1)  //是否锁定(false表示不锁定)
                .disabled(user.getEnabled()==0)   // 是否可用 (false表示可用)
                .build();
        // 7.千万别忘了返回 details!!!
        return details;
    }
}
