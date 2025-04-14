package cn.tedu.knows.faq.controller;


import cn.tedu.knows.commons.model.Question;
import cn.tedu.knows.faq.service.IQuestionService;
import cn.tedu.knows.faq.vo.QuestionVO;
import com.github.pagehelper.PageInfo;
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
 * 前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v2/questions")
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

    // 根据用户id查询问题数的Rest接口
    @GetMapping("/count")
    public Integer count(Integer userId){
        return questionService.countQuestionsByUserId(userId);
    }

    // (作业)根据用户id查询收藏数的Rest接口


    // 分页查询全部question数据的方法
    @GetMapping("/page")
    public List<Question> questions(Integer pageNum,
                                    Integer pageSize){
        PageInfo<Question> pageInfo=
                questionService.getQuestions(pageNum,pageSize);
        // 返回分页结果pageInfo对象中的List
        return pageInfo.getList();
    }

    // 根据每页条数计算总页数的Rest接口
    @GetMapping("/page/count")
    public int pageCount(Integer pageSize){
        // 目标查询总页数
        // 先要查询总条数,再根据pageSize值进行计算
        // MybatisPlus提供了查询总条数的业务逻辑层方法
        // questionService.count()就能实现
        int count=questionService.count();
        //return count%pageSize==0  ? count/pageSize
        //                          : count/pageSize+1;
        return (count+pageSize-1)/pageSize;
    }





}
