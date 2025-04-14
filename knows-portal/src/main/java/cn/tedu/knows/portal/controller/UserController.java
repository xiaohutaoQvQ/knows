package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private IUserService userService;

    // 返回所有讲师的控制器方法
    @GetMapping("/master")
    public List<User> master(){
        // 调用业务逻辑层方法获得所有讲师
        List<User> users=userService.getTeachers();
        return users;
    }

    // 查询当前登录用户的信息面板
    @GetMapping("/me")
    public UserVO me(
            @AuthenticationPrincipal UserDetails user){
        // 调用业务逻辑层方法
        UserVO userVO=userService.getUserVO(user.getUsername());
        return userVO;
    }


}
