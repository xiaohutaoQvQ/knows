package cn.tedu.knows.search.controller;

import cn.tedu.knows.search.service.IQuestionService;
import cn.tedu.knows.search.vo.QuestionVO;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v3/questions")
public class QuestionController {

    @Resource
    private IQuestionService questionService;

    @PostMapping
    public PageInfo<QuestionVO> search(
            String key,
            Integer pageNum,
            @AuthenticationPrincipal UserDetails user
            ){
        Integer pageSize=8;
        if (pageNum==null){
            pageNum=1;
        }
        PageInfo<QuestionVO> pageInfo=questionService.search(
                key,user.getUsername(),pageNum,pageSize
        );
        return pageInfo;
    }
}
