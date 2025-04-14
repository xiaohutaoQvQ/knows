package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.model.Question;
import cn.tedu.knows.portal.service.IQuestionService;
import cn.tedu.knows.portal.vo.QuestionVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v1/questions")
@Slf4j
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    // localhost:8080/v1/questions/my
    @GetMapping("/my")
    public PageInfo<Question> my(
            //@AuthenticationPrincipal注解效果
            // 从Spring-Security框架获得当前登录用户的UserDetails对象
            // 赋值给注解之后的参数
            @AuthenticationPrincipal UserDetails user,
            Integer pageNum
    ) {
        Integer pageSize = 8;
        if (pageNum == null) {
            pageNum = 1;
        }
        PageInfo<Question> pageInfo = questionService
                .getMyQuestions(user.getUsername(), pageNum, pageSize);
        return pageInfo;

    }

    // 学生发布问题访问的控制层方法
    // @PostMapping("")表明要访问此控制器方法
    // 需要以post方式访问localhost:8080/v1/questions
    @PostMapping("")
    public String createQuestion(
            @Validated QuestionVO questionVO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user
    ) {
        log.debug("接收表单信息:{}", questionVO);
        if (result.hasErrors()) {
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }

        // 这里调用业务逻辑层方法运行新增问题
        questionService.saveQuestion(questionVO, user.getUsername());
        return "ok";

    }

    // 查询讲师任务列表
    @GetMapping("/teacher")
    // 我们需要限制当前登录用户必须是讲师身份,才能查询任务列表
    // 使用Spring-Security提供的权限\角色的验证功能,实现限制效果
    // @PreAuthorize注解设置当前用户必须包含ROLE_TEACHER角色
    // 否则会发生没有权限访问的错误(403错误)
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public PageInfo<Question> teacher(
            @AuthenticationPrincipal UserDetails user,
            Integer pageNum){
        Integer pageSize=8;
        if(pageNum==null){
            pageNum=1;
        }
        // 调用业务逻辑层
        PageInfo<Question> pageInfo=questionService
          .getTeacherQuestions(user.getUsername(),pageNum,pageSize);
        return pageInfo;
    }

    // 根据id查询问题详情的控制器方法
    // "/{id}"是SpringMvc框架支持的一种占位符赋值的写法
    // 它可以匹配路径赋值的内容
    // localhost:8080/v1/questions/149
    // 那么这个{id}就会被149赋值
    @GetMapping("/{id}")
    // 使用控制器方法参数的特殊写法获得路径中的149
    public Question question(
            // 要想获得路径中{id}占位符的值
            // 1.必须编写 @PathVariable注解
            // 2.参数的名称必须和{}中的内容一致
            @PathVariable Integer id
    ){
        Question question=questionService.getQuestionById(id);
        return question;
    }

}
