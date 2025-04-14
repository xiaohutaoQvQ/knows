package cn.tedu.knows.sys.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/home")
public class HomeController {
    // Spring-Security框架中角色\权限是框架设置好的类型
    // 要判断具体的某个角色,建议将这个角色声明为常量类型,判断时使用
    public static final GrantedAuthority STUDENT=
            new SimpleGrantedAuthority("ROLE_STUDENT");
    public static final GrantedAuthority TEACHER=
            new SimpleGrantedAuthority("ROLE_TEACHER");
    // localhost:9000/v1/home
    @GetMapping
    public String index(
            @AuthenticationPrincipal UserDetails user){
        // 判断当前登录用户是否包含讲师角色
        if(user.getAuthorities().contains(TEACHER)){
            // 如果包含讲师角色,跳转到讲师首页
            return "/index_teacher.html";
        }else if(user.getAuthorities().contains(STUDENT)){
            // 如果不包含讲师角色,包含学生角色,跳转到学生首页
            return "/index_student.html";
        }
        // 既不是讲师也不是学生的用户暂不考虑,直接返回null
        return null;
    }

}
