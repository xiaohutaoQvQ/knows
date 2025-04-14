package cn.tedu.knows.faq.service.impl;


import cn.tedu.knows.commons.exception.ServiceException;
import cn.tedu.knows.commons.model.Answer;
import cn.tedu.knows.commons.model.Question;
import cn.tedu.knows.commons.model.User;
import cn.tedu.knows.faq.mapper.AnswerMapper;
import cn.tedu.knows.faq.mapper.QuestionMapper;
import cn.tedu.knows.faq.service.IAnswerService;
import cn.tedu.knows.faq.vo.AnswerVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {

    @Autowired
    private AnswerMapper answerMapper;

    @Resource
    private RibbonClient ribbonClient;

    @Override
    @Transactional
    public Answer saveAnswers(AnswerVO answerVO, String username) {
        // 根据用户名查询用户信息
        User user=ribbonClient.getUser(username);
        // 实例化Answer收集信息
        Answer answer=new Answer()
                .setContent(answerVO.getContent())
                .setQuestId(answerVO.getQuestionId())
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setLikeCount(0)
                .setAcceptStatus(0)
                .setCreatetime(LocalDateTime.now());
        // 执行新增并判断
        int num=answerMapper.insert(answer);
        if(num!=1){
            throw new ServiceException("数据库异常");
        }
        // 别忘了返回answer
        return answer;
    }

    @Override
    public List<Answer> getAnswersByQuestionId(Integer id) {
        // 执行关联查询,获得包含所有评论的回答列表
        List<Answer> answers=answerMapper
                        .findAnswersByQuestionId(id);
        // 别忘了返回
        return answers;
    }

    @Autowired
    private QuestionMapper questionMapper;
    @Override
    // 本方法要连续进行answer表和question表的两次修改,必须添加事务的支持
    @Transactional
    public boolean accept(Integer answerId, String username) {
        User user=ribbonClient.getUser(username);
        // 根据answerId查询出Answer对象
        Answer answer=answerMapper.selectById(answerId);
        // 根据answer对象的questId获得Question对象
        Question question=questionMapper
                        .selectById(answer.getQuestId());
        // 判断当前登录用户是不是问题的发布者
        if(user.getId().equals(question.getUserId())){
            // 身份判断无误,开始进行修改
            int num = answerMapper
                    .updateAcceptStatus(1,answerId);
            if(num!=1){
                throw new ServiceException("数据库异常");
            }
            // 修改完answer,开始修改question
            num=questionMapper.updateStatus(
                        Question.SOLVED,question.getId());
            if(num!=1){
                throw new ServiceException("数据库异常");
            }
            // 返回true,表示修改一切顺利
            return true;
        }
        // 程序运行到此处,表示没有进入上面的if
        // 也就是当前登录用户不是问题的发布者,返回false表示这个情况
        return false;
    }
}
