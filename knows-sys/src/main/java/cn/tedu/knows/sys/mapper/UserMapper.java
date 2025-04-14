package cn.tedu.knows.sys.mapper;


import cn.tedu.knows.commons.model.Permission;
import cn.tedu.knows.commons.model.Role;
import cn.tedu.knows.commons.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    // 根据用户id查询用户所有权限的方法
    @Select("SELECT p.id , p.name\n" +
            "FROM user u\n" +
            "LEFT JOIN user_role ur ON u.id=ur.user_id\n" +
            "LEFT JOIN role r       ON r.id=ur.role_id\n" +
            "LEFT JOIN role_permission rp\n" +
            "ON r.id=rp.role_id\n" +
            "LEFT JOIN permission p ON p.id=rp.permission_id\n" +
            "WHERE u.id=#{id}")
    List<Permission> findUserPermissionsById(Integer id);

    // 根据用户名查询用户信息
    @Select("select * from user where username=#{username}")
    User findUserByUsername(String username);

    // 查询所有讲师的方法
    @Select("select * from user where type=1")
    List<User> findTeachers();

    // 根据用户id查询用户所有角色的方法
    @Select("SELECT r.id , r.name\n" +
            "FROM user u\n" +
            "LEFT JOIN user_role ur ON u.id=ur.user_id\n" +
            "LEFT JOIN role      r  ON r.id=ur.role_id\n" +
            "WHERE u.id=#{id}")
    List<Role> findUserRolesById(Integer userId);


}


