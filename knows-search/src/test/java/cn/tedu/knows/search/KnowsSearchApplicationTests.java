package cn.tedu.knows.search;

import cn.tedu.knows.search.repository.ItemRepository;
import cn.tedu.knows.search.vo.Item;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class KnowsSearchApplicationTests {

    @Resource
    ItemRepository itemRepository;
    // 单增
    @Test
    void addOne() {
        Item item=new Item()
                .setId(1L)
                .setTitle("罗技激光无线游戏鼠标")
                .setCategory("鼠标")
                .setBrand("罗技")
                .setPrice(148.0)
                .setImage("/1.jpg");
        // 执行新增操作的方法 save()
        itemRepository.save(item);
        System.out.println("ok");
    }
    // 按id查询
    @Test
    void getOne(){
        // SpringData自带按id查询对象的方法
        // Optional是一个包装类型,能够保存查询出的结果
        Optional<Item> optional=itemRepository.findById(1L);
        // 需要查询结果内容时,从包装类中取出(get方法)即可
        System.out.println(optional.get());
    }

    // 批量增
    @Test
    void addList(){
        // 实例化一个List对象
        List<Item> list=new ArrayList<>();
        // 向List中添加Item对象
        list.add(new Item(2L,"罗技激光有线办公鼠标",
                "鼠标","罗技",68.0,
                "/2.jpg"));
        list.add(new Item(3L,"雷蛇机械无线游戏键盘",
                "键盘","雷蛇",318.0,
                "/3.jpg"));
        list.add(new Item(4L,"微软有线静音办公鼠标",
                "鼠标","微软",128.0,
                "/4.jpg"));
        list.add(new Item(5L,"罗技有线机械背光键盘",
                "键盘","罗技",236.0,
                "/5.jpg"));
        // 批量新增List
        itemRepository.saveAll(list);
        System.out.println("ok");
    }

    // 全查
    @Test
    void getAll(){
        Iterable<Item> items=itemRepository.findAll();
        /*for (Item item:items){
            System.out.println(item);
        }*/
        items.forEach(item-> System.out.println(item));
    }

    // 单条件查询
    @Test
    void queryOne(){
        // 查询title中包含"游戏"分词的Item对象
        Iterable<Item> items=itemRepository
                .queryItemsByTitleMatches("游戏机械");
        items.forEach(item -> System.out.println(item));
    }

    // 多条件查询
    @Test
    void queryTwo(){
        Iterable<Item> items=itemRepository
                .queryItemsByTitleMatchesAndBrandMatches(
                        "游戏","雷蛇");
        items.forEach(item -> System.out.println(item));
    }


    @Test
    void queryOrder(){
        Iterable<Item> items=itemRepository
                .queryItemsByTitleMatchesOrBrandMatchesOrderByPriceDesc("游戏","罗技");
        items.forEach(item -> System.out.println(item));
    }


    //分页查询测试
    @Test
    void queryPage(){
        int pageNum=1;
        int pageSize=2;
        Page<Item> page=itemRepository.
                queryItemsByTitleMatchesOrBrandMatchesOrderByPriceDesc(
                        "游戏","罗技",
                        PageRequest.of(pageNum-1,pageSize));
        page.forEach(item -> System.out.println(item));

        System.out.println("总页数"+page.getTotalPages());
        System.out.println("当前页"+page.getNumber());
        System.out.println("每页条数"+page.getSize());
        System.out.println("是不是首页"+page.isFirst());
        System.out.println("是不是尾页"+page.isLast());
    }

}
