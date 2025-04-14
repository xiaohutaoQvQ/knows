package cn.tedu.knows.portal.service;

import cn.tedu.knows.portal.model.Answer;
import cn.tedu.knows.portal.vo.AnswerVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface IAnswerService extends IService<Answer> {

    // 新增回答的业务逻辑层方法
    // 这个方法的返回值是新增成功的answer对象
    // 因为项目需要将新增成功的回答立即显示在回答列表中
    Answer saveAnswers(AnswerVO answerVO,String username);

    // 根据问题id查询所有回答的业务逻辑层方法
    List<Answer> getAnswersByQuestionId(Integer id);

    //根据回答id采纳答案的业务逻辑方法
    boolean accept(Integer answerId,String username);
}
