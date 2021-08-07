package org.mangolee.utils;

import org.springframework.web.server.ServerWebExchange;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class InterceptorUtils {
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
}
