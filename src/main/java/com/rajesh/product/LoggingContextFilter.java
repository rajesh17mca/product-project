package com.rajesh.product;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class LoggingContextFilter implements Filter {

    private static final Logger log =
            LogManager.getLogger(LoggingContextFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        long startTime = System.currentTimeMillis();
        String transactionId = UUID.randomUUID().toString();

        try {
            if (request instanceof HttpServletRequest httpRequest) {
                ThreadContext.put("uri", httpRequest.getRequestURI());
                ThreadContext.put("url", httpRequest.getRequestURL().toString());
                ThreadContext.put("action", "ActionStart");
                ThreadContext.put("transactionId", transactionId);
            }

            chain.doFilter(request, response);

        } finally {
            long duration = System.currentTimeMillis() - startTime;

            ThreadContext.put("action", "ActionEnd");
            ThreadContext.put("durationMs", String.valueOf(duration));
            log.info("HTTP request completed");

            // Always clean MDC
            ThreadContext.clearAll();
        }
    }
}
