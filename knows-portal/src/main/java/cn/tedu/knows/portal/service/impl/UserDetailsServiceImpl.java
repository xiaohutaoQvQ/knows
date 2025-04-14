package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.Permission;
import cn.tedu.knows.portal.model.Role;
import cn.tedu.knows.portal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

// 当前类要保存到Spring容器中以便Spring-Security使用
// 这个类实现的接口UserDetailsService,是Spring-Security框架
// 提供的一个接口,用于我们实现数据库登录
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    // loadUserByUsername方法是UserDetailsService接口中声明的
    // 方法的功能是根据用户输入的用户名返回这个用户的详情信息(详情\Details)
    // 返回值UserDetails也是Spring-Security提供的,
    // 使用Spring-Security框架识别的用户信息
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 方法的参数就是用户登录时输入的用户名
        // 1. 根据用户名获得用户对象
        User user=userMapper.findUserByUsername(username);
        // 2. 验证用户对象是不是null
        if(user==null){
            // 如果是null,表示用户名不存在,返回null表示登录失败
            return null;
        }
        // 3. 根据用户id查询所有权限
        List<Permission> permissions=userMapper
                             .findUserPermissionsById(user.getId());
        // 4. 将权限集合转换为String数组
        String[] auth=new String[permissions.size()];
        int i=0;
        for(Permission p:permissions){
            auth[i]=p.getName();
            i++;
        }
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        // 查询当前登录用户所有角色,将角色也添加到auth数组中
        // 根据用户id查询所有角色
        List<Role> roles=userMapper.findUserRolesById(user.getId());
        // 数组扩容,以保证能够保存角色信息
        auth= Arrays.copyOf(auth,auth.length+roles.size());
        //  auth={"/add","/delete","/save","/update",null}
        // 将角色信息保存到auth中
        for (Role role:roles){
            auth[i]=role.getName();
            i++;
        }
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        // 5. 创建UserDetails对象,保存信息获得用户详情
        UserDetails details= org.springframework.security.core.
                userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(auth)
                // 设置当前账号是否锁定,false表示不锁定
                .accountLocked(user.getLocked()==1)
                // 设置当前账号是否可用,false表示可用
                .disabled(user.getEnabled()==0)
                .build();
        // 6. 返回用户详情UserDetails对象
        // 千万别忘了返回details
        return details;
    }
}
