package org.mangolee.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.tomcat.util.http.MimeHeaders;
import org.mangolee.exception.BaseException;
import org.mangolee.service.RedisServiceImp;
import org.mangolee.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Value("${spring.application.name}")
    private String applicationName;
    //@Autowired
    private RedisServiceImp redisServiceImp = new RedisServiceImp();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String consumer = request.getHeader("consumer-service");
        if(consumer == null)
        {
            consumer = getIpAddress(request);
        }
        if(token == null)
        {
            PrintWriter writer = null;
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
            String msg ="Authorization error: no Token found when <"+ consumer+
                    "> called <" + applicationName + ">'s #"+
                    ((HandlerMethod)handler).getMethod().getName()+" Handler.";
            Result<Void> result = new Result(403, msg, null);
            String jsonString = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
            try {
                writer = response.getWriter();
                writer.print(jsonString);
            } catch (IOException e) {
                throw new BaseException(result);
            } finally {
                if (writer != null)
                    writer.close();
            }
            return false;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("message","helloworld");
        modifyHeaders(map,request);
        return true;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip ==null || ip.length() ==0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip ==null || ip.length() ==0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip ==null || ip.length() ==0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip ==null || ip.length() ==0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip ==null || ip.length() ==0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip ==null || ip.length() ==0 ||"unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if(ip!=null){
            ip=ip.split(",")[0];
        }
        return ip;
    }

    private void modifyHeaders(Map<String, String> headerses, HttpServletRequest request) {
        if (headerses == null || headerses.isEmpty()) {
            return;
        }
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        try {
            Field request1 = requestClass.getDeclaredField("request");
            request1.setAccessible(true);
            Object o = request1.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object o1 = coyoteRequest.get(o);
            Field headers = o1.getClass().getDeclaredField("headers");
            headers.setAccessible(true);
            MimeHeaders o2 = (MimeHeaders)headers.get(o1);
            for (Map.Entry<String, String> entry : headerses.entrySet()) {
                o2.removeHeader(entry.getKey());
                o2.addValue(entry.getKey()).setString(entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
