package org.caillou.company.logme.spring.web.log4j.filter;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Enumeration<String> headerNames = httpRequest.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                if(name.equalsIgnoreCase("X-LOGGING-TRACE")){
                    ThreadLocal<Boolean> threadLocal = new ThreadLocal<>();
                    threadLocal.set(true);
                    ThreadContext.put("TRACE_IT", "FULL_TRACE_ENABLED");
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
