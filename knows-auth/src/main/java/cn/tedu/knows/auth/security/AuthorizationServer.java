package cn.tedu.knows.auth.security;

import cn.tedu.knows.auth.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
// 这个注解表示当前类时Oauth2标准下实现的授权服务器配置类
// 表示启动授权服务器相关功能
@EnableAuthorizationServer
public class AuthorizationServer extends
        AuthorizationServerConfigurerAdapter {
    // 添加依赖注入的对象,它们大多是之前课程中向Spring容器中保存准备好的配置
    // Spring-Security框架的授权管理器,Oauth2要使用
    @Resource
    private AuthenticationManager authenticationManager;
    // 要登录肯定需要登录配置类
    @Resource
    private UserDetailsServiceImpl userDetailsService;

    // 核心配置方法1:配置授权服务器的各种参数
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // Oauth2框架提供了很多控制器方法
        // 所以我们配置的就是这些控制器方法运行的内容
        // endpoints(端点)参数其实就是控制器方法的功能

        // 配置登录时的授权管理器
        endpoints.authenticationManager(authenticationManager)
                // 设置登录配置类
                .userDetailsService(userDetailsService)
                // 配置登录允许的提交方式
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                // 配置令牌生成器对象
                .tokenServices(tokenService());
    }
    // 注入保存令牌配置的对象
    @Resource
    private TokenStore tokenStore;
    // 添加客户端详情对象(框架提供的对象,不是我们写的)
    @Resource
    private ClientDetailsService clientDetailsService;
    // Spring容器保存令牌生成器对象
    @Resource
    private JwtAccessTokenConverter accessTokenConverter;
    @Bean
    public AuthorizationServerTokenServices tokenService(){
        // 实例化 令牌生成器对象
        DefaultTokenServices services=
                new DefaultTokenServices();
        // 设置令牌保存策略
        services.setTokenStore(tokenStore);


        //实例化一个令牌增强对象
        TokenEnhancerChain chain=new TokenEnhancerChain();
        //
        chain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        //
        services.setTokenEnhancer(chain);


        // 设置令牌有效期(单位是秒,1800就是半小时)
        services.setAccessTokenValiditySeconds(3600);
        // 指定生成令牌的客户端,设置客户端详情
        services.setClientDetailsService(clientDetailsService);
        // 最后返回令牌生成器对象
        return services;
    }

    // 获得加密对象,用于下面方法中的加密操作
    @Resource
    private PasswordEncoder passwordEncoder;

    // 核心配置方法2:配置客户端对应的各种权限
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置当前授权服务器支持的所有客户端
        // 以及客户端的权限
        // 因为我们现在只有一个<<达内知道>>项目,所以只配置这一个客户端
        // 客户端比较少可以直接保存在内存
        // 如果客户端比较多,可能需要连接数据库获得权限信息
        clients.inMemory()   // 内存中保存客户端信息
                .withClient("knows")    //定义客户端名称
                // 客户端定义的加密密码
                .secret(passwordEncoder.encode("123456"))
                .scopes("main")
                .authorizedGrantTypes("password");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll")

                .checkTokenAccess("permitAll")

                .allowFormAuthenticationForClients();
    }
}
