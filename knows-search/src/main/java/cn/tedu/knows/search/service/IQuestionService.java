package cn.tedu.knows.search.service;

import cn.tedu.knows.search.vo.QuestionVO;
import com.github.pagehelper.PageInfo;

public interface IQuestionService {
    // 声明同步数据库question表到ES中的业务逻辑层方法
    void syncData();

    //
    PageInfo<QuestionVO> search(String key, String username, Integer pageNum, Integer pageSize);
}
