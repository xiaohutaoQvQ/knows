package cn.tedu.knows.sys.controller;


import cn.tedu.knows.commons.model.User;
import cn.tedu.knows.sys.service.IUserService;
import cn.tedu.knows.sys.vo.RegisterVO;
import cn.tedu.knows.sys.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@Slf4j
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

    @PostMapping("/register")
    public String register(
            // 我们可以通过添加@Validated注解启动SpringValidation的验证
            // 一旦在控制器方法参数前添加@Validated,表示控制器方法运行前
            // 先由SpringValidation框架按照RegisterVO类中编写的验证规则进行验证
            @Validated RegisterVO registerVO,
            // 下面的参数就是SpringValidation框架验证的结果对象
            // 对registerVO对象属性的验证信息会自动保存到result对象中
            BindingResult result
    ) {
        // 使用@Slf4j提供的log对象,将registerVO信息输出到日志
        log.debug("接收到表单信息:{}", registerVO);
        // 判断result对象中有没有验证失败的信息
        if (result.hasErrors()) {
            // 进入if表示registerVO对象中有属性没有通过验证
            // 下面来获取这个信息(信息就是验证未通过的message的值)
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }
        userService.registerStudent(registerVO);
        return "ok";
    }

}
