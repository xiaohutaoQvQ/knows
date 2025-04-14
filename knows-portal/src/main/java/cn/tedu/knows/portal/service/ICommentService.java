package cn.tedu.knows.portal.service;

import cn.tedu.knows.portal.model.Comment;
import cn.tedu.knows.portal.vo.CommentVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface ICommentService extends IService<Comment> {

    // 新增评论的业务逻辑层方法
    Comment saveComment(CommentVO commentVO, String username);

    // 按id删除评论的业务逻辑层方法
    boolean removeComment(Integer id, String username);

    // 按id修改评论的业务逻辑层方法
    Comment updateComment(Integer commentId, CommentVO commentVO,
                          String username);


}
