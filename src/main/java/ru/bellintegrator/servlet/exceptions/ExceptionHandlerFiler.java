package ru.bellintegrator.servlet.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

public class ExceptionHandlerFiler implements Filter {

    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void init(FilterConfig filterConfig) {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error(((HttpServletRequest) request).getRequestURL(), e);
            writeResponse(response, e);
        }
    }


    @Override
    public void destroy() {
        //do nothing
    }

    private void writeResponse(ServletResponse response, Exception e) throws IOException {
        try (PrintWriter responseWriter = response.getWriter()) {
            ObjectMapper mapper = new ObjectMapper();
            String errorMessage = (e.getMessage() == null || e.getMessage().isEmpty())
                    ? "Internal Server Error"
                    : e.getMessage();
            mapper.writeValue(responseWriter, new ErrorView(errorMessage));
        }
    }
}
