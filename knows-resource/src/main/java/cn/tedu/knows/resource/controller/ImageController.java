package cn.tedu.knows.resource.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Slf4j
@CrossOrigin// 跨域
public class ImageController {

    // 从application.properties文件中获得配置信息
    @Value("${knows.resource.path}")
    private File resourcePath;
    @Value("${knows.resource.host}")
    private String resourceHost;

    // 上传图片必须是post
    @PostMapping()
    public String upload(MultipartFile imageFile) throws IOException {
        // 我们要确定文件\图片保存的位置
        // 文件保存的路径是F:/upload/年/月/日
        // 先获得当前日期的字符串做路径
        // path: 2022/03/11
        String path = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(LocalDateTime.now());
        // 确定要上传的文件夹
        // folder= F:/upload/2022/03/11
        File folder = new File(resourcePath, path);
        // 创建这个文件夹
        folder.mkdirs();  //mkdirsssssssss!!!!!!
        // 下面要确定要上传文件的文件名
        // 获得用户原始文件名,以便获得后缀名
        String filename = imageFile.getOriginalFilename();
        // kas.jd.kk.jpg
        // 0123456789
        // ext=     .jpg
        String ext = filename.substring(filename.lastIndexOf("."));
        // 随机生成文件名
        // jhsdjf-uwey-udsf-jahdjkfhajdskf.jpg
        String name = UUID.randomUUID().toString() + ext;
        // 最终文件位置(路径名+文件名)
        // F:/upload/2022/03/11/jhsdjf-uwey-udsf-jahdjkfhajdskf.jpg
        File file = new File(folder, name);
        // 利用日志将实际上传文件的路径输出到控制台
        log.debug("文件上传到:{}", file.getAbsolutePath());
        // 执行上传
        imageFile.transferTo(file);
        // 为了实现回显,我们需要返回可以访问上传的图片的路径
        // 我们上传的图片要想访问,需要访问静态资源服务器的路径,可能的格式如下
        // http://localhost:8899/2022/03/14/xxx-xxx-xxx.jpg
        //      resourceHost    /  path    /     name
        String url = resourceHost + "/" + path + "/" + name;
        log.debug("回显图片的路径为:{}",url);
        // 返回成功上传文件的静态资源服务器路径
        return url;
    }
}
