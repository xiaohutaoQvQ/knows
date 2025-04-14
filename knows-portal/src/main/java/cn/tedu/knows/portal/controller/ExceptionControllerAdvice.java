package cn.tedu.knows.portal.controller;

import cn.tedu.knows.portal.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 这个注解表示指定控制器方法运行到特殊时间节点时
// 可以运行的额外代码的方法,我们这里只考虑控制器方法发生异常时运行的代码
//             ↓↓↓↓↓↓
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    // 下面编写一个注解,表示该方法是专门处理控制器发生的异常的
    @ExceptionHandler
    // 返回值为String,设计为当发生异常时,将异常信息返回为axios
    // 方法名称随意,标准是handleXXXXXException
    // 参数指定控制器发生什么异常类型时运行这个方法
    public String handleServiceException(ServiceException e){
        // 这个方法中的代码等价于控制器方法中try-catch结构中的catch
        log.error("发生业务异常",e);
        return e.getMessage();
    }

    // 再编写一个处理其他异常的方法
    // 直接声明为异常的父类类型即可
    @ExceptionHandler
    public String handleException(Exception e){
        log.error("发生其他异常",e);
        return e.getMessage();
    }




}
