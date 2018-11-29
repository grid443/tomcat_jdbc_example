package ru.bellintegrator.servlet.person;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import ru.bellintegrator.servlet.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PersonsListServlet extends BaseServlet {
    private final Log log = LogFactory.getLog(getClass());

    /**
     * Save list of persons to database
     *
     * @param request  list of person data as JSON
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuffer requestUrl = request.getRequestURL();
        log.info(String.format("request:%s;start", requestUrl));

        String requestBody = readBody(request);
        log.debug(String.format("request:%s;body:%s", requestUrl, requestBody));

        ObjectMapper mapper = new ObjectMapper();
        List<PersonView> persons = mapper.readValue(
                requestBody,
                new TypeReference<List<PersonView>>() {
                }
        );

        log.info(String.format("request:%s;mapping request body success", requestUrl));

        PersonDao dao = new PersonDaoImpl(dataSource());
        dao.addAll(persons);

        log.info(String.format("request:%s;success", requestUrl));
    }
}
