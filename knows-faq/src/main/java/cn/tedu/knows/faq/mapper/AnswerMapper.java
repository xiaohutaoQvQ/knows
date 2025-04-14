package cn.tedu.knows.faq.mapper;



import cn.tedu.knows.commons.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Repository
public interface AnswerMapper extends BaseMapper<Answer> {

    // 对应AnswerMapper.xml文件中的内容
    // 根据问题id查询所有回答以及回答包含的评论的方法
    // 方法名必须和xml文件中<select>标签的id一致
    List<Answer> findAnswersByQuestionId(Integer questionId);


    @Update("update answer set accept_status=#{acceptStatus} where id=#{answerId}")
    int updateAcceptStatus(@Param("acceptStatus") Integer acceptStatus, @Param("answerId") Integer answerId);
}
