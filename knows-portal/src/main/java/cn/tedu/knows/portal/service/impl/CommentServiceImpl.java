package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.mapper.CommentMapper;
import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.model.Comment;
import cn.tedu.knows.portal.model.User;
import cn.tedu.knows.portal.service.ICommentService;
import cn.tedu.knows.portal.vo.CommentVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public Comment saveComment(CommentVO commentVO, String username) {
        User user = userMapper.findUserByUsername(username);
        Comment comment = new Comment()
                .setContent(commentVO.getContent())
                .setAnswerId(commentVO.getAnswerId())
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setCreatetime(LocalDateTime.now());
        System.out.println(comment);
        int num = commentMapper.insert(comment);
        if (num != 1) {
            throw new ServiceException("数据库忙");
        }
        // 千万别忘了返回comment
        return comment;
    }

    @Override
    @Transactional
    public boolean removeComment(Integer id, String username) {
        User user = userMapper.findUserByUsername(username);
        if (user.getType().equals(1)) {
            int num = commentMapper.deleteById(id);
            return num == 1;
        }
        Comment comment = commentMapper.selectById(id);
        if (user.getId().equals(comment.getUserId())) {
            int num = commentMapper.deleteById(id);
            return num == 1;
        }

        throw new ServiceException("您不能删除别人的评论");
    }

    @Override
    public Comment updateComment(Integer commentId, CommentVO commentVO, String username) {
        User user = userMapper.findUserByUsername(username);
        Comment comment = commentMapper.selectById(commentId);
        if (user.getType().equals(1) || user.getId().equals(comment.getUserId())) {
            comment.setContent(commentVO.getContent());
            int num = commentMapper.updateById(comment);
            if (num != 1) {
                throw new ServiceException("数据库忙");
            }
            return comment;
        }
        throw new ServiceException("您不能修改别人的评论!");
    }


}
