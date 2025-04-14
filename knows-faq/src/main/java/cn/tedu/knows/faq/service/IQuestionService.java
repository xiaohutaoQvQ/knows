package cn.tedu.knows.faq.service;


import cn.tedu.knows.commons.model.Question;
import cn.tedu.knows.faq.vo.QuestionVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface IQuestionService extends IService<Question> {

    // 根据登录用户的用户名查询问题列表
    // 返回值修改为PageInfo既能包含list,又能包含分页信息
    // 添加分页参数pageNum页码,和PageSize每页条数,用于分页查询
    PageInfo<Question> getMyQuestions(String username,
                                      Integer pageNum, Integer pageSize);

    // 用户发布问题的业务逻辑层方法
    void saveQuestion(QuestionVO questionVO, String username);

    // 分页查询讲师任务列表
    PageInfo<Question> getTeacherQuestions(String username,
                                           Integer pageNum, Integer pageSize);

    // 根据问题id 查询问题详情
    Question getQuestionById(Integer id);

    // 根据用户id查询问题数
    Integer countQuestionsByUserId(Integer userId);

    //Integer countCollectionsByUserId(Integer userId);

    // 分页查询所有question数据的方法
    PageInfo<Question> getQuestions(Integer pageNum,
                                    Integer pageSize);

}
