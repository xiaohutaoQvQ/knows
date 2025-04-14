package cn.tedu.knows.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor         // 生成全参构造
@NoArgsConstructor          // 生成无参构造
// @Document是SpringData提供的注解
// 指定当前类对应ES中索引的名称,如果对ES操作时
// 指定名称的索引没有创建,SpringData会自动创建该索引
@Document(indexName = "items")
public class Item implements Serializable {

    // SpringData框架标记主键的注解@Id
    @Id
    private Long id;
    // type = FieldType.Text表示要分词的字符串
    @Field(type = FieldType.Text,
            analyzer = "ik_max_word",
            searchAnalyzer = "ik_max_word")
    private String title;
    // type = FieldType.Keyword表示不需要分词的字符串
    @Field(type = FieldType.Keyword)
    private String category;    // 分类
    @Field(type = FieldType.Keyword)
    private String brand;       // 品牌
    @Field(type = FieldType.Double)
    private Double price;       // 价格

    // 2022/03/28/hdsjjhsajd.jpg
    // 因为当前商品图片地址不会成为搜索条件,所以编写index = false
    // 实现只保存信息,不生成索引,来节省一定空间
    @Field(type = FieldType.Keyword,index = false)
    private String image;       // 图片地址

}
