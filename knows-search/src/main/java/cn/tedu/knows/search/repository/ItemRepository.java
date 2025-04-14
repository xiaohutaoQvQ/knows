package cn.tedu.knows.search.repository;

import cn.tedu.knows.search.vo.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

// Spring家族的框架下都将数据访问层称之为Repository
@Repository
public interface ItemRepository
        extends ElasticsearchRepository<Item,Long> {
    // ElasticsearchRepository接口的泛型<[ES数据封装类],[主键类型]>
    // 一旦我们编写的接口继承了这个父接口
    // 我们的ItemRepository就自带了基本的增删改查功能

    // 自定义查询
    // 除了SpringData提供的基本方法,当我们需要按照个性化的逻辑来查询时
    // 就需要使用自定义查询
    // SpringData框架支持使用方法名称直接表示查询逻辑
    // SpringData框架会自动按方法名称生成查询语句进行查询返回结果
    // 所以方法名称有既定的语法格式,必须严格按照语法格式编写
    // query:表示查询(类似select关键字)
    // Items\Item:表示返回值(Item表示返回的类,带s是集合,不带s是对象)
    // by:根据什么条件查询(类似where)
    // Title:指定查询的属性名
    // Matches:相当于sql中的like,即模糊查询
    Iterable<Item> queryItemsByTitleMatches(String title);

    // 多条件查询
    // 使用And或Or表示查询逻辑
    Iterable<Item> queryItemsByTitleMatchesAndBrandMatches(
            String title,String brand);


    //排序查询
    Iterable<Item> queryItemsByTitleMatchesOrBrandMatchesOrderByPriceDesc(
            String title,String brand
    );

    // 分页查询
    Page<Item>
    queryItemsByTitleMatchesOrBrandMatchesOrderByPriceDesc(
            String title, String brand, Pageable pageable);

}
