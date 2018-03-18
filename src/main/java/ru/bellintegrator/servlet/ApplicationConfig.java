package ru.bellintegrator.servlet;

import org.apache.catalina.Context;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.h2.Driver;

import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ApplicationConfig {

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String CONTEXT_NAME = "java:/comp/env";
    public static final String H2_DATA_SOURCE_NAME = "jdbc/h2DataSource";

    private static final Log log = LogFactory.getLog(ApplicationConfig.class);

    private static final String CONTEXT_PATH = "";
    private static final String APP_BASE = ".";
    private static final String DEFAULT_PORT = "8080";
    private static final String DATA_BASE_SCHEMA = "schema.sql";

    public static void main(String[] args) throws Exception {
        int port = serverPort(args);
        Tomcat tomcat = buildServer(port);
        StandardContext context = buildContext(tomcat);

        registerServlet(context, new PersonsServlet(), "persons", "/persons");
        registerServlet(context, new PingServlet(), "ping", "/ping");

        ContextResource resource = buildResource(
                H2_DATA_SOURCE_NAME,
                DataSource.class.getName(),
                h2DatasourceProperties()
        );


        context.getNamingResources().addResource(resource);

        context.addServletContainerInitializer(new DataBaseInitializer(DATA_BASE_SCHEMA), null);

        tomcat.start();

        log.info("Application started: http://" + tomcat.getServer().getAddress() + ":" + port);

        tomcat.getServer().await();
    }

    private static Tomcat buildServer(int port) {
        Tomcat tomcat = new Tomcat();
        tomcat.enableNaming();
        tomcat.setPort(port);
        return tomcat;
    }

    private static StandardContext buildContext(Tomcat tomcat) {
        return (StandardContext) tomcat.addContext(CONTEXT_PATH, Paths.get(APP_BASE).toAbsolutePath().toString());
    }

    private static void registerServlet(Context context, HttpServlet servlet, String servletName, String urlPattern) {
        Tomcat.addServlet(context, servletName, servlet);
        context.addServletMappingDecoded(urlPattern, servletName);
    }

    private static Map<String, Object> h2DatasourceProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put("factory", BasicDataSourceFactory.class.getName());
        properties.put("driverClassName", Driver.class.getName());
        properties.put("url", "jdbc:h2:mem:practice_db");
        properties.put("username", "sa");
        properties.put("password", "123456");

        return properties;
    }

    private static ContextResource buildResource(String jndiName, String type, Map<String, Object> properties) {
        ContextResource resource = new ContextResource();
        resource.setName(jndiName);
        resource.setAuth("Container");
        resource.setType(type);

        for (Map.Entry<String, Object> property : properties.entrySet()) {
            resource.setProperty(property.getKey(), property.getValue());
        }

        return resource;
    }

    private static int serverPort(String[] args) {
        if (args == null || args.length < 1) {
            return Integer.valueOf(DEFAULT_PORT);
        }

        return Integer.valueOf(args[0]);
    }
}