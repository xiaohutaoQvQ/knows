package cn.tedu.knows.sys.service;


import cn.tedu.knows.commons.model.Permission;
import cn.tedu.knows.commons.model.Role;
import cn.tedu.knows.commons.model.User;
import cn.tedu.knows.sys.vo.RegisterVO;
import cn.tedu.knows.sys.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface IUserService extends IService<User> {

    // Ctrl+Alt+B 快速定位到当前接口的实现类

    // 定义学生注册功能的业务逻辑层方法
    void registerStudent(RegisterVO registerVO);

    // 查询所有讲师的业务逻辑层方法
    List<User> getTeachers();
    // 查询所有讲师Map的业务逻辑层方法
    Map<String,User> getTeacherMap();

    // 根据用户名查询UserVO用户信息面板
    UserVO getUserVO(String username);

    // 根据用户名获得用户对象
    User getUserByUsername(String username);

    // 根据用户id获得所有权限
    List<Permission> getPermissionsById(Integer id);
    // 根据用户id获得所有角色
    List<Role> getRolesById(Integer id);



}
