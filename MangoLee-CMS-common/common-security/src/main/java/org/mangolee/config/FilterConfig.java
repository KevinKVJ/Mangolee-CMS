package org.mangolee.config;

import org.mangolee.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FilterConfig {
    @Bean
    public TokenFilter getTokenFilter()
    {
        return new TokenFilter();
    }
    @Autowired
    private TokenFilter tokenFilter;
    @Value(value = "${filter-config.use-token:false}")
    private Boolean useToken;
    @Value(value = "${filter-config.token-match:null}")
    private List<String> tokenMatch;
    @Bean
    public FilterRegistrationBean TokenFilterRegistration() {
        FilterRegistrationBean registration = registration = new FilterRegistrationBean();
        if(useToken != null && useToken)//当使用token拦截器
        {
            //注入过滤器
            registration.setFilter(tokenFilter);
            //过滤器名称
            registration.setName("TokenFilter");
            //过滤器顺序
            registration.setOrder(1);
            //当没有写明匹配路径时默认匹配所有路径
            if(tokenMatch == null ||
                    (tokenMatch.size() ==1 &&
                            tokenMatch.get(0).trim().equals("null")))//当未设置拦截路径时自动采用
            {
                tokenMatch = new ArrayList<>();
                tokenMatch.add("/*");
            }
            for(String matchPath:tokenMatch)
            {
                registration.addUrlPatterns(matchPath);
            }
        }
        else
        {
            registration.setFilter(new Filter() {
                @Override
                public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                    chain.doFilter(request,response);return; }});
            //过滤器名称
            registration.setName("NullFilter");
            //过滤器顺序
            registration.setOrder(0);
            registration.addUrlPatterns("/**");
        }
        return registration;
    }
}
