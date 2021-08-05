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

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
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
        if(useToken != null && useToken)
        {
            tokenRegistration = registry.addInterceptor(tokenInterceptor);
            if(tokenMatch == null ||
                    (tokenMatch.size() ==1 &&
                            tokenMatch.get(0).trim().equals("null")))
            {
                tokenMatch = new ArrayList<>();
                tokenMatch.add("/**");
            }
            tokenRegistration.addPathPatterns(tokenMatch);
            if(tokenExclude!= null &&
                    !(tokenExclude.size() == 1 &&
                            tokenExclude.get(0).trim().equals("null")))
            {
                tokenRegistration.excludePathPatterns(tokenExclude);
            }
        }
    }
}
