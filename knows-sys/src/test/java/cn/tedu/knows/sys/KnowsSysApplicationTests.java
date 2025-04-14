package cn.tedu.knows.sys;

import cn.tedu.knows.commons.model.User;
import cn.tedu.knows.sys.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KnowsSysApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
        User user=userMapper.findUserByUsername("st2");
        System.out.println(user);
    }

}
