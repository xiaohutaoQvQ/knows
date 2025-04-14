package cn.tedu.knows.faq.mapper;


import cn.tedu.knows.commons.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author tedu.cn
* @since 2022-03-04
*/
    @Repository
    public interface QuestionMapper extends BaseMapper<Question> {
    //根据用户id查询问题数
    @Select("SELECT COUNT(*) FROM question\n" +
            "WHERE user_id=#{id} AND delete_status=0")
    int countQuestionsByUserId(Integer id);
    //作业:完成根据用户id
    @Select("SELECT COUNT(*) FROM user_collect\n" +
            "WHERE user_id=#{id}")
    int countCollectsByUserId(Integer id);

    //2022-03-15
    // 根据讲师id查询讲师任务列表
    @Select("SELECT q.* FROM question q\n" +
            "LEFT JOIN user_question uq ON q.id=uq.question_id\n" +
            "WHERE uq.user_id=#{id} OR q.user_id=#{id}\n" +
            "ORDER BY q.createtime DESC")
    List<Question> findTeacherQuestions(Integer id);

    // 修改问题状态的方法
    @Update("update question set status=#{status} " +
            " where id=#{questionId}")
    int updateStatus(@Param("status") Integer status,
                     @Param("questionId") Integer questionId);

}
