package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.model.Answer;
import cn.tedu.knows.portal.service.IAnswerService;
import cn.tedu.knows.portal.vo.AnswerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v1/answers")
@Slf4j
public class AnswerController {

    @Autowired
    private IAnswerService answerService;

    // 新增讲师回复功能的控制器方法
    // 路径为:localhost:8080/v1/answers
    @PostMapping("")
    // 只有讲师(或持有回答权限)的用户才能回复问题
    // 方法一:判断用户是否是讲师身份
    //@PreAuthorize("hasRole('TEACHER')")
    // 方法二:判读用户是否拥有回答权限
    @PreAuthorize("hasAuthority('/question/answer')")
    public Answer postAnswer(
            @Validated AnswerVO answerVO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user
            ){
        log.debug("接收表单信息:{}",answerVO);
        if(result.hasErrors()){
            String msg=result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        // 调用业务逻辑层方法
        Answer answer=answerService
                .saveAnswers(answerVO,user.getUsername());
        return answer;
    }

    // 根据问题id查询所有回答的方法
    // /v1/answers/question/149
    @GetMapping("/question/{id}")
    public List<Answer> questionAnswers(
            @PathVariable Integer id){
        List<Answer> answers=answerService
                    .getAnswersByQuestionId(id);
        return answers;
    }


    //采纳答案的控制层方法
    @GetMapping("/{answerId}/solved")
    public String solved (
            @PathVariable Integer answerId,
            @AuthenticationPrincipal UserDetails user){
        boolean accepted=answerService.accept(answerId,user.getUsername());
        if (accepted){
            return "采纳完成";
        }else {
            return "您不能采纳别人的问题";
        }
    }

}


