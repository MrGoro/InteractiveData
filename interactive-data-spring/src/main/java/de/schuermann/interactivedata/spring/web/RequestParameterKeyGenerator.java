package de.schuermann.interactivedata.spring.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Philipp Schürmann
 */
@Component("RequestParameterKeyGenerator")
public class RequestParameterKeyGenerator implements KeyGenerator {

    private static final Log log = LogFactory.getLog(RequestParameterKeyGenerator.class);

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String key = "";
        for(Object param : params) {
            if(param instanceof HttpServletRequest) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) param;
                key += httpServletRequest.getRequestURI();
                if(httpServletRequest.getQueryString() != null) {
                    key += "?" + httpServletRequest.getQueryString();
                }
            } else {
                key += param.toString()+"/";
            }
        }
        log.debug("Using cache key [" + key + "] for request");
        return key;
    }
}