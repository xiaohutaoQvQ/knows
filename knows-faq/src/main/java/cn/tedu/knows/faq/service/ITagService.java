package cn.tedu.knows.faq.service;


import cn.tedu.knows.commons.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
public interface ITagService extends IService<Tag> {

    // 定义全查所有标签的业务逻辑层方法
    List<Tag> getTags();

    // 定义返回包含所有标签对象Map的方法
    Map<String,Tag> getTagMap();

}
