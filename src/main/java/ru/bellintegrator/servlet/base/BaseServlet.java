package ru.bellintegrator.servlet.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.bellintegrator.servlet.config.ApplicationConfig;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseServlet extends HttpServlet {

    protected DataSource dataSource() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup(ApplicationConfig.CONTEXT_NAME);
            return (DataSource) envContext.lookup(ApplicationConfig.H2_DATA_SOURCE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("getting data source error", e);
        }
    }

    protected String readBody(HttpServletRequest request) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            final StringBuilder builder = new StringBuilder();

            reader.lines().forEach(line -> {
                builder.append(line);
                builder.append("\n");
            });

            return builder.toString();
        }
    }

    protected void writeResponse(HttpServletResponse response, Object data) {
        try (PrintWriter responseWriter = response.getWriter()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(responseWriter, data);
        } catch (IOException e) {
            throw new RuntimeException("building response error", e);
        }
    }
}
