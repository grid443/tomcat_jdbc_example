package ru.bellintegrator.servlet.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Filter sets default charset encoding for request and response
 */
public class DefaultCharsetFilter implements Filter {
    public static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_CONTENT_TYPE = "application/json;charset=utf-8";

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        request.setCharacterEncoding(DEFAULT_CHARSET);

        response.setCharacterEncoding(DEFAULT_CHARSET);
        response.setContentType(DEFAULT_CONTENT_TYPE);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
