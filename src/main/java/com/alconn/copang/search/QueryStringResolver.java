package com.alconn.copang.search;

import com.alconn.copang.annotations.QueryStringBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class QueryStringResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper mapper;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(QueryStringBody.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
//        req.setCharacterEncoding("UTF-8");
        String query = req.getQueryString();
        if (query == null) {
            return null;
        }
        query = URLDecoder.decode(query, "UTF-8");
        log.debug("query String parameter :{} ",  query);
        final String json = qs2json(URLDecoder.decode(query, "UTF-8"));
        return mapper.readValue(json, parameter.getParameterType());
    }

    private String qs2json(String a) {
        String res = "{\"";
//        res += a.replaceAll("[=]", "\":\"")
//            .replaceAll("[&]", "\",\"");

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == '=') {
                res += "\"" + ":" + "\"";
            } else if (a.charAt(i) == '&') {
                res += "\"" + "," + "\"";
            } else {
                res += a.charAt(i);
            }
        }
        res += "\"" + "}";
        return res;
    }
}
