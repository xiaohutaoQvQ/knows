package cn.tedu.knows.portal.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class QuestionVO implements Serializable {

    @NotBlank(message = "标签不能为空")
    @Pattern(regexp = "^.{3,50}$",message = "标题需要3~50个字符")
    private String title;
    //@NotEmpty 专门用于判断集合或数组非空的注解
    @NotEmpty(message = "请至少选择一个标签")
    private String[] tagNames={};
    @NotEmpty(message = "请至少选择一个讲师")
    private String[] teacherNicknames={};
    @NotBlank(message = "问题内容不能为空")
    private String content;

}
