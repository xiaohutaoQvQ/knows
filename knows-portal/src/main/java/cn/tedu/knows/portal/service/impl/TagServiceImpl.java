package cn.tedu.knows.portal.service.impl;

import cn.tedu.knows.portal.mapper.TagMapper;
import cn.tedu.knows.portal.model.Tag;
import cn.tedu.knows.portal.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-03-04
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    private TagMapper tagMapper;

    @Resource
    private RedisTemplate<String,List<Tag>> redisTemplate;

    @Override
    public List<Tag> getTags() {
        List<Tag> tags = redisTemplate.opsForValue().get("tags");
        if (tags==null){
            tags=tagMapper.selectList(null);
            redisTemplate.opsForValue().set("tags",tags);
            System.out.println("re");
        }
        return tags;
    }

    @Override
    public Map<String, Tag> getTagMap() {
        Map<String,Tag> tagMap= new HashMap<>();
        for (Tag t : getTags()){
            tagMap.put(t.getName(),t);
        }
        return tagMap;
    }
}
