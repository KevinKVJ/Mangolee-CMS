package org.mangolee.config;

import org.mangolee.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 将拦截器注册进容器，并根据配置文件决定是否使用，如何使用
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    //自动填充token拦截器
    @Bean
    public TokenInterceptor getTokenInterceptor(){
        return new TokenInterceptor();
    }
    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Value(value = "${interceptor-config.use-token:false}")
    private Boolean useToken;
    @Value(value = "${interceptor-config.token-match:null}")
    private List<String> tokenMatch;
    @Value(value = "${interceptor-config.token-exclude:null}")
    private List<String> tokenExclude;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration tokenRegistration =null;
        if(useToken != null && useToken)//当使用token拦截器
        {
            tokenRegistration = registry.addInterceptor(tokenInterceptor);
            if(tokenMatch == null ||
                    (tokenMatch.size() ==1 &&
                            tokenMatch.get(0).trim().equals("null")))//当未设置拦截路径时自动采用
            {
                tokenMatch = new ArrayList<>();
                tokenMatch.add("/**");
            }
            tokenRegistration.addPathPatterns(tokenMatch);
            if(tokenExclude!= null &&
                    !(tokenExclude.size() == 1 &&
                            tokenExclude.get(0).trim().equals("null")))//当设置了例外路径
            {
                tokenRegistration.excludePathPatterns(tokenExclude);
            }
        }
    }
}
