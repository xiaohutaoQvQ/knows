package cn.tedu.knows.faq.service.impl;


import cn.tedu.knows.commons.exception.ServiceException;
import cn.tedu.knows.commons.model.Comment;
import cn.tedu.knows.commons.model.User;
import cn.tedu.knows.faq.mapper.CommentMapper;
import cn.tedu.knows.faq.service.ICommentService;
import cn.tedu.knows.faq.vo.CommentVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Resource
    private RibbonClient ribbonClient;

    @Override
    @Transactional
    public Comment saveComment(CommentVO commentVO, String username) {
        User user=ribbonClient.getUser(username);
        Comment comment=new Comment()
                .setContent(commentVO.getContent())
                .setAnswerId(commentVO.getAnswerId())
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setCreatetime(LocalDateTime.now());
        System.out.println(comment);
        int num=commentMapper.insert(comment);
        if(num!=1){
            throw new ServiceException("数据库忙");
        }
        // 千万别忘了返回comment
        return comment;
    }

    @Override
    @Transactional
    public boolean removeComment(Integer id, String username) {
        User user=ribbonClient.getUser(username);
        // 判断是不是讲师用户
        if(user.getType().equals(1)){
            // 如果是讲师可以删除任何人的评论
            int num=commentMapper.deleteById(id);
            // num如果是1,返回true
            return num==1;
        }
        // 没有运行上面的if证明当前用户不是讲师,
        // 需要判断当前用户是否为评论的发布者,如果是才能删除该评论
        // 所以要先根据要删除的评论id查询评论对象
        Comment comment=commentMapper.selectById(id);
        // 判断当前登录用户是不是评论的发布者
        if(user.getId().equals(comment.getUserId())){
            // 如果匹配,表示当前用户就是评论的发布者,执行删除
            int num=commentMapper.deleteById(id);
            return num==1;
        }
        // 既不是讲师也不是评论的发布者,就抛出异常信息
        throw new ServiceException("您不能删除别人的评论!");

    }

    @Override
    @Transactional
    public Comment updateComment(Integer commentId, CommentVO commentVO, String username) {
        User user=ribbonClient.getUser(username);
        // 修改之前需要先将要修改的评论对象查询出来
        Comment comment=commentMapper.selectById(commentId);
        // 如果是讲师或者是评论的发布者,可以修改评论
        if(user.getType().equals(1) ||
                user.getId().equals(comment.getUserId())){
            // 执行content属性的修改
            comment.setContent(commentVO.getContent());
            // 执行修改,提交修改属性到数据库中
            int num=commentMapper.updateById(comment);
            if(num!=1){
                throw new ServiceException("数据库忙");
            }
            // 修改成功返回修改后的对象
            return comment;
        }
        throw new ServiceException("您不能修改别人的评论!");
    }
}
