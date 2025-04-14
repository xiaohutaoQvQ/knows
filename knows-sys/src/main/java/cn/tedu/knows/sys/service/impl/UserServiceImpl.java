package cn.tedu.knows.sys.service.impl;


import cn.tedu.knows.commons.exception.ServiceException;
import cn.tedu.knows.commons.model.*;
import cn.tedu.knows.sys.mapper.ClassroomMapper;
import cn.tedu.knows.sys.mapper.UserMapper;
import cn.tedu.knows.sys.mapper.UserRoleMapper;
import cn.tedu.knows.sys.service.IUserService;
import cn.tedu.knows.sys.vo.RegisterVO;
import cn.tedu.knows.sys.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    // 转到业务逻辑层实现类
    // 1.按快捷键(Alt+Enter)生成要实现的方法
    // 2.思考当前实现类方法需要哪些其它的依赖注入,声明它(已经声明的不用重复声明)
    // 3.编写业务逻辑层实现方法的代码

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional
    public void registerStudent(RegisterVO registerVO) {
        // 1.根据用户输入的邀请码查询班级信息
        QueryWrapper<Classroom> query=new QueryWrapper<>();
        query.eq("invite_code",registerVO.getInviteCode());
        Classroom classroom=classroomMapper.selectOne(query);
        // 2.判断班级信息是否为空,如果为空抛出异常,终止注册
        if(classroom==null){
            throw new ServiceException("邀请码错误");
        }
        // 3.根据用户输入的手机号,查询用户信息
        User user=userMapper.findUserByUsername(registerVO.getPhone());
        // 4.查询出的用户信息如果不为空,证明手机号已经被注册了,抛出异常终止注册
        if(user!=null){
            throw new ServiceException("手机号已经被注册");
        }
        // 5.将用户输入的密码加密为bcrypt
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        String pwd="{bcrypt}"+encoder.encode(registerVO.getPassword());
        // 6.实例化一个学生用户对象,向对象中赋值
        User stu=new User()
                .setUsername(registerVO.getPhone())
                .setNickname(registerVO.getNickname())
                .setPassword(pwd)
                .setClassroomId(classroom.getId())
                .setCreatetime(LocalDateTime.now())
                .setEnabled(1)
                .setLocked(0)
                .setType(0);
        // 7.执行用户对象的新增
        int num=userMapper.insert(stu);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }
        // 8.创建UserRole关系对象,并新增到数据库
        UserRole userRole=new UserRole()
                .setRoleId(2)
                .setUserId(stu.getId());
        num=userRoleMapper.insert(userRole);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }
    }

    //声明两个讲师缓存的集合
    private List<User> teachers=new CopyOnWriteArrayList<>();
    private Map<String,User> teacherMap=new ConcurrentHashMap<>();
    @Override
    public List<User> getTeachers() {
        if(teachers.isEmpty()){
            synchronized (teachers){
                teachers.clear();
                teacherMap.clear();
                List<User> list=userMapper.findTeachers();
                teachers.addAll(list);
                for(User u:list){
                    teacherMap.put(u.getNickname(),u);
                }
            }
        }
        // 最后别忘了返回teachers
        return  teachers;
    }

    @Override
    public Map<String, User> getTeacherMap() {
        if(teacherMap.isEmpty()){
            getTeachers();
        }
        return teacherMap;
    }

@Resource
private RestTemplate restTemplate;
    @Override
    public UserVO getUserVO(String username) {
        // 根据用户名查询用户
        User user=userMapper.findUserByUsername(username);
        // 根据用户id查询问题数 和收藏数(作业)
        String url = "http://faq-service/v2/questions/count?userId={1}";
        Integer count = restTemplate.getForObject(
                url,Integer.class,user.getId()
        );

        // (作业)调用方法获得当前用户的收藏数......

        // 实例化UserVO对象赋值并返回
        UserVO userVO=new UserVO()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setNickname(user.getNickname())
                .setQuestions(count);
        //(作业) 赋值收藏数到userVO对象
        // 别忘了返回userVO
        return userVO;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public List<Permission> getPermissionsById(Integer id) {
        return userMapper.findUserPermissionsById(id);
    }

    @Override
    public List<Role> getRolesById(Integer id) {
        return userMapper.findUserRolesById(id);
    }


}
