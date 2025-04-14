package cn.tedu.knows.faq;

import cn.tedu.knows.commons.model.Tag;
import cn.tedu.knows.faq.mapper.TagMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class KnowsFaqApplicationTests {

    @Autowired
    TagMapper tagMapper;
    @Test
    void contextLoads() {
        List<Tag> tags=tagMapper.selectList(null);
        for (Tag t: tags){
            System.out.println(t);
        }
    }

    @Resource
    RedisTemplate<String,String> redisTemplate;

    @Test
    //新增数据
    public void add(){
        redisTemplate.opsForValue().set("myname","zzz");
        System.out.println("ok");

    }

    @Test
    //获取数据
    public void gett(){
        String name = redisTemplate.opsForValue().get("myname");
        System.out.println(name);
    }

}
