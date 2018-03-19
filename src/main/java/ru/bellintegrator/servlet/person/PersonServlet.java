package ru.bellintegrator.servlet.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
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
import java.util.List;

public class PersonServlet extends HttpServlet {
    private final Log log = LogFactory.getLog(getClass());

    /**
     * List person objects from database
     *
     * @param request
     * @param response persons list as JSON
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer requestUrl = request.getRequestURL();
        log.info("request: " + requestUrl + ";start");

        PersonDao dao = new PersonDaoImpl(dataSource());
        List<PersonView> persons = dao.all();

        log.info("request: " + requestUrl + ";persons loaded count:" + persons.size());

        writeResponse(response, persons);

        log.info("request: " + requestUrl + ";success");
    }

    /**
     * Save new person to database
     *
     * @param request  person data as JSON
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuffer requestUrl = request.getRequestURL();
        log.info("request: " + requestUrl + ";start");

        String requestBody = readBody(request);
        log.debug("request:" + requestUrl + ";body:" + requestBody);

        ObjectMapper mapper = new ObjectMapper();
        PersonView person = mapper.readValue(requestBody, PersonView.class);

        log.info("request: " + requestUrl + ";mapping request body success");

        PersonDao dao = new PersonDaoImpl(dataSource());
        dao.add(person);

        log.info("request: " + requestUrl + ";success");
    }

    private DataSource dataSource() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup(ApplicationConfig.CONTEXT_NAME);
            return (DataSource) envContext.lookup(ApplicationConfig.H2_DATA_SOURCE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("getting data source error", e);
        }
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

    private void writeResponse(HttpServletResponse response, Object data) {
        try (PrintWriter responseWriter = response.getWriter()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(responseWriter, data);
        } catch (IOException e) {
            throw new RuntimeException("building response error", e);
        }
    }
}
