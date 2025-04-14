package cn.tedu.knows.portal;

import cn.tedu.knows.portal.mapper.ClassroomMapper;
import cn.tedu.knows.portal.model.Classroom;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QueryTest {
    @Autowired
    ClassroomMapper classroomMapper;
    @Test
    public void query(){
        QueryWrapper<Classroom> query=new QueryWrapper<>();

        query.eq("invite_code","JS2001-706246");
        Classroom classroom=classroomMapper.selectOne(query);
        System.out.println(classroom);
    }
}
