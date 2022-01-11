package org.mangolee.filter;

import org.mangolee.entity.Result;
import org.mangolee.service.RedisService;
import org.mangolee.utils.FilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class TokenFilter implements Filter {
    @Value("${spring.application.name}")
    private String applicationName;
    @Autowired
    private RedisService redisService;
    @Override


    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        String token = req.getHeader("token");
        String consumer = req.getHeader("consumer-service");
        if(consumer == null)//若header不带消费服务的信息，则去获取其访问ip
        {
            consumer = FilterUtils.getIpAddress(req);
        }
        if(token == null)//若未找到token项，则编辑json信息返回
        {

            String msg ="Authorization error: no Token found when <"+ consumer+
                    "> called <" + applicationName + ">'s #"+
                    req.getMethod()+" Handler.";
            Result<Void> result = new Result(403, msg, null);
            FilterUtils.writeResult2Response(result,(HttpServletResponse) response);
            return ;
        }
        //TODO 试图获取userinfo
        Result<String> verifyRes = redisService.getValue(token);
        if(verifyRes.getCode()!=200 || verifyRes.getData() == null)
        {
            String msg ="Authorization error: Token not found in Redis because: "
                    +verifyRes.getMessage()+",when <"+ consumer+
                    "> called <" + applicationName + ">'s #"+
                    req.getMethod()+" Handler.";
            Result<Void> result = new Result(403, msg, null);
            FilterUtils.writeResult2Response(result,(HttpServletResponse) response);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        //TODO 试图填充获取的userinfo各个字段进header
        map.put("message","helloworld");
        FilterUtils.modifyHeaders(map,req);
        chain.doFilter(request,response);
        return;
    }

    @Override
    public void destroy() {
    }
}
