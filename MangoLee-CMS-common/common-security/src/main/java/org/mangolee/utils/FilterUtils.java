package org.mangolee.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.tomcat.util.http.MimeHeaders;
import org.mangolee.entity.Result;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FilterUtils {
    /**
     * 反射爆破遍历pojo类，只适合单层
     * @param pojo
     * @return
     * @throws IllegalAccessException
     */
    static public Map<String,Object> reflectPojo(Object pojo) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = pojo.getClass().getDeclaredFields();
        for(Field field:fields)
        {
            field.setAccessible(true);
            map.put(field.getName(),field.get(pojo));
        }
        return map;
    }

    /**
     * 获取请求来源ip
     * @param request
     * @return
     */
    static public String getIpAddress(HttpServletRequest request) {
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

    /**
     * 对request类添加header项
     * @param headerses
     * @param request
     */
    static public void modifyHeaders(Map<String, String> headerses, HttpServletRequest request) {
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

    /**
     * 将结果对象写入相应体中
     * @param result
     * @param response
     */
    static public void writeResult2Response(Result result, HttpServletResponse response)
    {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        String jsonString = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
        try {
            writer = response.getWriter();
            writer.print(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(result.getMessage());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}