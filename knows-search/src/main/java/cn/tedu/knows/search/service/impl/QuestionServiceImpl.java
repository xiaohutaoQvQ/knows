package cn.tedu.knows.search.service.impl;

import cn.tedu.knows.commons.model.User;
import cn.tedu.knows.search.repository.QuestionRepository;
import cn.tedu.knows.search.service.IQuestionService;
import cn.tedu.knows.search.utils.Pages;
import cn.tedu.knows.search.vo.QuestionVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;

// 别忘了添加响应注解
@Service
@Slf4j
public class QuestionServiceImpl implements IQuestionService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private QuestionRepository questionRepository;
    @Override
    public void syncData() {
        // 通过Ribbon调用获得总页数
        String url=
                "http://faq-service/v2/questions/page/count?pageSize={1}";
        int pageSize=8;
        Integer total=restTemplate.getForObject(
                url, Integer.class , pageSize);
        // 根据总页数循环调用查询每页的信息
        for (int i=1 ; i<=total ; i++){
            // 循环中i就是当前循环要查询的页数
            url="http://faq-service/v2/questions" +
                    "/page?pageNum={1}&pageSize={2}";
            QuestionVO[] questions=restTemplate.getForObject(
                    url,QuestionVO[].class, i,pageSize);
            // 将每页信息新增到ES
            questionRepository.saveAll(Arrays.asList(questions));
            log.debug("完成了第{}页的新增",i);
        }
    }

    @Override
    public PageInfo<QuestionVO> search(String key, String username, Integer pageNum, Integer pageSize) {
        //  根据用户名查询用户对象
        String url="http://sys-service/v1/auth/user?username={1}";
        User user=restTemplate.getForObject(
                url,User.class,username);
        // 定义分页条件和排序规则的Pageable对象
        Pageable pageable= PageRequest.of(
                pageNum-1,pageSize,
                Sort.Direction.DESC,"createtime");
        // 调用数据访问层进行查询
        Page<QuestionVO> page=questionRepository
                .queryAllByParams(key,key,user.getId(),pageable);
        // 将page转换为PageInfo返回
        return Pages.pageInfo(page);
    }
}
