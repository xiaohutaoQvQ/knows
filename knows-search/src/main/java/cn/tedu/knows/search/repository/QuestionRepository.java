package cn.tedu.knows.search.repository;


import cn.tedu.knows.search.vo.QuestionVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// 数据访问层注解@Repository必须要添加
@Repository
public interface QuestionRepository
        extends ElasticsearchRepository<QuestionVO,Integer> {
    @Query("{\n" +
            "    \"bool\": {\n" +
            "      \"must\": [{\n" +
            "        \"bool\": {\n" +
            "          \"should\": [\n" +
            "          {\"match\": {\"title\": \"?0\"}}, \n" +
            "          {\"match\": {\"content\": \"?1\"}}]\n" +
            "        }\n" +
            "      }, {\n" +
            "        \"bool\": {\n" +
            "          \"should\": [\n" +
            "          {\"term\": {\"publicStatus\": 1}}, \n" +
            "          {\"term\": {\"userId\": ?2}}]\n" +
            "        }\n" +
            "      }]\n" +
            "    }\n" +
            "  }")
    Page<QuestionVO> queryAllByParams(
            String title, String content,
            Integer userId, Pageable pageable);

}
