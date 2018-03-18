package ru.bellintegrator.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PersonsServlet extends HttpServlet {
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        log.info("request: " + request.getRequestURL());

        response.setCharacterEncoding(ApplicationConfig.DEFAULT_CHARSET);

        try {
            DataSource dataSource = dataSource();

            try (Connection conn = dataSource.getConnection(); PrintWriter responseWriter = response.getWriter()) {
                Statement statement = conn.createStatement();

                String query = "SELECT id, first_name, age FROM Person";
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    responseWriter.println("--------------------------------------------");
                    responseWriter.println("        ID:" + resultSet.getLong("id"));
                    responseWriter.println("FIRST NAME:" + resultSet.getString("first_name"));
                    responseWriter.println("       AGE:" + resultSet.getLong("age"));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Response processing error", e);
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("request: " + request.getRequestURL());

        request.setCharacterEncoding(ApplicationConfig.DEFAULT_CHARSET);
        response.setCharacterEncoding(ApplicationConfig.DEFAULT_CHARSET);

        String requestBody = readBody(request);
        log.debug("request body:" + requestBody);

        ObjectMapper mapper = new ObjectMapper();
        PersonView person = mapper.readValue(requestBody, PersonView.class);
        log.info(person);
    }

    private DataSource dataSource() throws Exception {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup(ApplicationConfig.CONTEXT_NAME);
        return (DataSource) envContext.lookup(ApplicationConfig.H2_DATA_SOURCE_NAME);
    }

    private String readBody(HttpServletRequest request) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            final StringBuilder builder = new StringBuilder();

            reader.lines().forEach(line -> {
                builder.append(line);
                builder.append("\n");
            });

            return builder.toString();
        }
    }
}
