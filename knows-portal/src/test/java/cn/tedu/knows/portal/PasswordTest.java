package cn.tedu.knows.portal;

import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.Permission;
import cn.tedu.knows.portal.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

// 必须添加@SpringBootTest注解
@SpringBootTest
public class PasswordTest {

    // 声明密码加密操作对象
    PasswordEncoder encoder=new BCryptPasswordEncoder();
    // 加密测试
    @Test
    public void pwd(){
        String str="123456";
        // 执行加密,使用encode方法参数是要加密的字符串,返回值是加密结果
        String pwd= encoder.encode(str);
        System.out.println(pwd);
        //$2a$10$kVuKchYUN92TGcQb./H.iOHCT8LOIho12bI1OTb5xPTassynjdsES
        //$2a$10$wX.WG4MS/3SXY6MuSo54KuSRtqDQD2OZx83PEVc/VTcAb2O7Yv4zW
    }

    // 验证操作
    @Test
    public void match(){
        // 验证一个字符串是否能够加密为指定的加密结果
        // 方法matches([原字符串],[加密字符串])返回值是boolean类型
        // 返回值为真表示验证通过,返回值为假表示验证失败
        boolean b=encoder.matches("123456",
                "$2a$10$wX.WG4MS/3SXY6MuSo54KuSRtqDQD2OZx83PEVc/VTcAb2O7Yv4zW");
        System.out.println("验证结果为:"+b);

    }

    @Autowired
    UserMapper userMapper;
    @Test
    public void userTest(){
        User user =userMapper.findUserByUsername("st2");
        List<Permission> list = userMapper.findUserPermissionsById(user.getId());
        System.out.println(user);
        for (Permission p :list){
            System.out.println(p);
        }
    }


}
