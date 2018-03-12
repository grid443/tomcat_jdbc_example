package ru.bellintegrator.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for checking application availability
 */
public class PingServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter responseWriter = response.getWriter()) {
            responseWriter.println("pong");
        }
    }
}
