package cn.tedu.knows.faq.controller;


import cn.tedu.knows.commons.model.Tag;
import cn.tedu.knows.faq.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/v2/tags")
public class TagController {

    // 添加业务逻辑层的依赖注入
    @Autowired
    private ITagService tagService;

    // @GetMapping("")写法的含义就是只使用类上定义的路径作为当前控制方法的路径
    // localhost:8080/v1/tags
    @GetMapping("")
    public List<Tag> tags(){
        List<Tag> tags=tagService.getTags();
        return tags;
    }

}
