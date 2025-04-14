package cn.tedu.knows.auth;

import cn.tedu.knows.auth.filter.CorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class KnowsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowsAuthApplication.class, args);
    }

    //添加
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    //
    @Bean
    public FilterRegistrationBean registrationBean(){
        FilterRegistrationBean<CorsFilter> bean=new FilterRegistrationBean<>();
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.setFilter(new CorsFilter());
        return bean;
    }
}
