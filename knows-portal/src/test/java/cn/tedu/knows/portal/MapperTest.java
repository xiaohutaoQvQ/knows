package cn.tedu.knows.portal;

import cn.tedu.knows.portal.mapper.AnswerMapper;
import cn.tedu.knows.portal.model.Answer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private AnswerMapper answerMapper;
    @Test
    public void testAnswer(){
        // 根据问题id查询所有回答,包含所有评论
        List<Answer> answers=
                answerMapper.findAnswersByQuestionId(149);
        for (Answer a: answers){
            System.out.println(a);
        }
    }

}
