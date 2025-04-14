package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.exception.ServiceException;
import cn.tedu.knows.portal.mapper.QuestionMapper;
import cn.tedu.knows.portal.mapper.QuestionTagMapper;
import cn.tedu.knows.portal.mapper.UserMapper;
import cn.tedu.knows.portal.mapper.UserQuestionMapper;
import cn.tedu.knows.portal.model.*;
import cn.tedu.knows.portal.service.IQuestionService;
import cn.tedu.knows.portal.service.ITagService;
import cn.tedu.knows.portal.service.IUserService;
import cn.tedu.knows.portal.vo.QuestionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<Question> getMyQuestions(String username,
                                   Integer pageNum,Integer pageSize) {
        // 先根据用户名查询用户信息(用户对象)
        User user=userMapper.findUserByUsername(username);
        // 再使用QueryWrapper完成该用户的问题列表的查询
        QueryWrapper<Question> query=new QueryWrapper<>();
        query.eq("user_id",user.getId());
        query.eq("delete_status",0);
        query.orderByDesc("createtime");
        //PageHelper.startPage就是设置分页查询的方法
        // 在查询执行之前设置,设置后,这次查询就会自动分页
        // startPage([页码],[每页条数])
        // 页码从1开始,第二页就写2即可
        PageHelper.startPage(pageNum,pageSize);
        // 执行查询
        List<Question> list=questionMapper.selectList(query);
        // 遍历当前查询到的所有问题对象
        for(Question question:list){
            // 将当前问题对象的tagNames转换为List<Tag>
            List<Tag> tags=tagNamesToTags(question.getTagNames());
            // 将转换结果得到的list赋值给当前question对象的tags属性
            question.setTags(tags);
        }
        // 别忘了返回PageInfo
        return new PageInfo<>(list);
    }

    // 编写TagNames转换为List<Tag>的方法
    // 需要获得ITagService业务逻辑层中的缓存Map
    @Autowired
    private ITagService tagService;
    private List<Tag> tagNamesToTags(String tagNames){
        //tagNames:"Java基础,Java SE,面试题"
        String[] names=tagNames.split(",");
        // names:{"Java基础","Java SE","面试题"}
        Map<String,Tag> tagMap=tagService.getTagMap();
        // 循环遍历之前声明一个用于接收返回值的List
        List<Tag> tags=new ArrayList<>();
        // 遍历当前的names数组
        for(String name:names){
            // 根据标签名称获得标签对象
            Tag t=tagMap.get(name);
            // 将获得的标签对象赋值到tags中
            tags.add(t);
        }
        // 返回tags
        return tags;

    }

    @Autowired
    private IUserService userService;
    @Autowired
    private QuestionTagMapper questionTagMapper;
    @Autowired
    private UserQuestionMapper userQuestionMapper;

    @Override
    // Spring声明式事务
    // 标记之后,当前被标记的方法中所有数据库操作保存在一个事务中
    // 方法运行正常,事务提交,数据库操作生效,方法运行发生异常,事务回滚,数据库操作撤销
    // 今后只要是包含两个以及两个以上的数据库增删改操作的方法,都必须添加该注解
    @Transactional
    public void saveQuestion(QuestionVO questionVO, String username) {
        // 1.根据用户名获得用户信息
        User user=userMapper.findUserByUsername(username);
        // 2.将用户选中的标签数组拼接为标签名称字符串
        // {"Java基础","Java SE","面试题"}
        // 目标"Java基础,Java SE,面试题"
        StringBuilder builder=new StringBuilder();
        for(String tagName:questionVO.getTagNames()){
            builder.append(tagName).append(",");
        }
        // "Java基础,Java SE,面试题," 多了个"," 要删除
        builder.deleteCharAt(builder.length()-1);
        //  builder转换成字符串
        String tagNames=builder.toString();
        // 3.收集Question信息,实例化并赋值
        Question question=new Question()
                .setTitle(questionVO.getTitle())
                .setContent(questionVO.getContent())
                .setUserNickName(user.getNickname())
                .setUserId(user.getId())
                .setCreatetime(LocalDateTime.now())
                .setStatus(0)
                .setPageViews(0)
                .setPublicStatus(0)
                .setDeleteStatus(0)
                .setTagNames(tagNames);
        // 4.新增question到数据库
        int num=questionMapper.insert(question);
        if(num!=1){
            throw new ServiceException("数据库忙");
        }
        // 5.新增question_tag关系表
        Map<String,Tag> tagMap=tagService.getTagMap();
        // 遍历用户选中的所有标签
        for(String tagName : questionVO.getTagNames()){
            // 根据当前标签名称获得对应的标签对象
            Tag t=tagMap.get(tagName);
            // 实例化QuestionTag实体类,并赋值
            QuestionTag questionTag=new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(t.getId());
            // 执行新增流程
            num=questionTagMapper.insert(questionTag);
            if(num!=1){
                throw new ServiceException("数据库忙!");
            }
            log.debug("新增问题和标签的关系:{}",questionTag);
        }
        // 6.新增user_question关系表
        Map<String,User> teacherMap=userService.getTeacherMap();
        for(String nickname:questionVO.getTeacherNicknames()){
            User teacher=teacherMap.get(nickname);
            UserQuestion userQuestion=new UserQuestion()
                    .setQuestionId(question.getId())
                    .setUserId(teacher.getId())
                    .setCreatetime(LocalDateTime.now());
            num=userQuestionMapper.insert(userQuestion);
            if(num!=1){
                throw new ServiceException("数据库忙");
            }
            log.debug("新增了问题和讲师的关系:{}",userQuestion);
        }
    }

    @Override
    public PageInfo<Question> getTeacherQuestions(String username, Integer pageNum, Integer pageSize) {
        // 根据用户名获得用户信息
        User user=userMapper.findUserByUsername(username);
        // 设置分页条件
        PageHelper.startPage(pageNum,pageSize);
        // 查询任务列表
        List<Question> list=questionMapper
                .findTeacherQuestions(user.getId());
        // 遍历当前查询出的任务列表,将问题的tags属性赋值
        for(Question question:list){
            List<Tag> tags=tagNamesToTags(question.getTagNames());
            question.setTags(tags);
        }
        // 别忘了返回pageInfo
        return new PageInfo<>(list);
    }

    @Override
    public Question getQuestionById(Integer id) {
        // 根据MybatisPlus提供的方法来查询Question对象
        Question question=questionMapper.selectById(id);
        List<Tag> tags=tagNamesToTags(question.getTagNames());
        question.setTags(tags);
        // 别忘了返回question
        return question;
    }
}
