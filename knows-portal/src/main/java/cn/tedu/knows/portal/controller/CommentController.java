package cn.tedu.knows.portal.controller;


import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.model.Comment;
import cn.tedu.knows.portal.service.ICommentService;
import cn.tedu.knows.portal.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v1/comments")
@Slf4j
public class CommentController {

    @Autowired
    private ICommentService commentService;

    // @PostMapping等价于@PostMapping("")
    @PostMapping
    public Comment postComment(
            @Validated CommentVO commentVO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user
            ){
        log.debug("接收到表单信息:{}",commentVO);
        if(result.hasErrors()){
            String msg=result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        // 这里调用业务逻辑层
        Comment comment=commentService.saveComment(
                commentVO,user.getUsername());
        // 千万别忘了返回新增成功的评论对象comment
        return comment;
    }

    // 按评论id删除评论的方法
    // /v1/comments/20/delete
    @GetMapping("/{id}/delete")
    public String removeComment(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails user){
        boolean isDelete=commentService
                .removeComment(id,user.getUsername());
        if(isDelete){
            return "ok";
        }else{
            return "删除失败,评论可能已经被删除了!";
        }
    }

    // 按id进行评论内容修改的方法
    // /v1/comments/20/update
    @PostMapping("/{id}/update")
    public Comment updateComment(
            @PathVariable Integer id,
            @Validated CommentVO commentVO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user){
        log.debug("表单信息:{}",commentVO);
        log.debug("要修改的评论id:{}",id);
        if(result.hasErrors()){
            String msg=result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        // 调用业务逻辑层
        Comment comment=commentService
                .updateComment(id, commentVO, user.getUsername());
        // 返回comment
        return comment;

    }





}
