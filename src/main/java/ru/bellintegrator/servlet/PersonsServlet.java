package ru.bellintegrator.servlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PersonsServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

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

    private DataSource dataSource() throws Exception {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup(ApplicationConfig.CONTEXT_NAME);
        return (DataSource) envContext.lookup(ApplicationConfig.H2_DATA_SOURCE_NAME);
    }
}
