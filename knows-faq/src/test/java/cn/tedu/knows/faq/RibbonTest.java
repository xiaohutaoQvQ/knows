package cn.tedu.knows.faq;

import cn.tedu.knows.commons.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class RibbonTest {
    @Resource
    RestTemplate restTemplate;
    @Test
    public void ribbon(){
        String url="http://sys-service/v1/auth/demo";

        restTemplate.getForObject(url,String.class);
    }

    @Test
    public void getUser(){
        String url="http://sys-service/v1/auth/user?username={1}";
        User user = restTemplate.getForObject(url,User.class,"st2");
        System.out.println(user);
    }
}
