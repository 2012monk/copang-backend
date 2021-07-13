package com.alconn.copang.cache;

import com.alconn.copang.search.ItemSearchCondition;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.springframework.cache.interceptor.KeyGenerator;

public class SearchKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        Object condition = Arrays.stream(params).filter(o -> o.getClass().isAssignableFrom(
            ItemSearchCondition.class)).findFirst().orElseThrow(IllegalArgumentException::new);
        Field[] f = condition.getClass().getDeclaredFields();
        for (Field fi: f){
            fi.setAccessible(true);
            Object p = null;
            try {
                p = fi.get(condition);
                if (p != null) sb.append(";").append(p.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
