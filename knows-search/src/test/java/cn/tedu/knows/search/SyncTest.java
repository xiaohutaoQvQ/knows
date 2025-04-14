package cn.tedu.knows.search;

import cn.tedu.knows.search.repository.QuestionRepository;
import cn.tedu.knows.search.service.IQuestionService;
import cn.tedu.knows.search.vo.QuestionVO;

import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

@SpringBootTest
public class SyncTest {

    @Resource
    IQuestionService questionService;

    @Test
    void run(){
        questionService.syncData();
    }

    @Resource
    QuestionRepository questionRepository;

    @Test

    void getAll(){
        Iterable<QuestionVO> qs=questionRepository.findAll();
        qs.forEach(q-> System.out.println(q));
    }

    //测试搜索查询的数据访问层方法
    @Test
    public void search(){
        Page<QuestionVO> questions = questionRepository
                .queryAllByParams("java","java",11, PageRequest.of(0,8));
        questions.forEach(q-> System.out.println(q));
    }


    //
    @Test
    void testService(){
        PageInfo<QuestionVO> pageInfo = questionService.search("java","st2",1,8);
        pageInfo.getList().forEach(q-> System.out.println(q));
    }
}
