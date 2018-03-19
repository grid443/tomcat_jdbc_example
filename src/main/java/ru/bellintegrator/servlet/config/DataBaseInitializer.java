package ru.bellintegrator.servlet.config;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class DataBaseInitializer implements ServletContainerInitializer {
    private final Log log = LogFactory.getLog(getClass());

    private final String schemaFileLocation;

    DataBaseInitializer(String schemaFileLocation) {
        this.schemaFileLocation = schemaFileLocation;
    }


    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) {
        log.info("DataBase schema initialization start. " + schemaFileLocation);

        try {
            Set<String> sqlStatements = readSqlStatements();
            executeSqlStatements(sqlStatements);

            log.info("Database schema initialized. " + schemaFileLocation);
        } catch (Exception e) {
            throw new RuntimeException("Database schema initialization error", e);
        }
    }

    private Set<String> readSqlStatements() throws Exception {
        StringBuilder statementsBuilder = new StringBuilder();
        Set<String> sqlStatements = new HashSet<>();

        log.info("reading file " + schemaFileLocation + " start");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        getClass().getClassLoader().getResourceAsStream(schemaFileLocation),
                        DefaultCharsetFilter.DEFAULT_CHARSET
                )
        )) {
            reader.lines().forEach(
                    line -> {
                        if (line.isEmpty() && statementsBuilder.length() > 0) {
                            buildLine(statementsBuilder, sqlStatements);
                            statementsBuilder.setLength(0);
                            return;
                        }

                        statementsBuilder.append(line);
                        statementsBuilder.append(" ");
                    }
            );
        }

        if (statementsBuilder.length() > 0) {
            buildLine(statementsBuilder, sqlStatements);
        }

        log.info("reading file " + schemaFileLocation + " success");

        return sqlStatements;
    }

    private void buildLine(StringBuilder statementsBuilder, Set<String> sqlStatements) {
        String statement = statementsBuilder.toString().trim();

        if (statement.endsWith(";")) {
            statement = statement.substring(0, statement.length() - 1);
        }

        sqlStatements.add(statement);
    }

    private DataSource dataSource() throws Exception {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup(ApplicationConfig.CONTEXT_NAME);
        return (DataSource) envContext.lookup(ApplicationConfig.H2_DATA_SOURCE_NAME);
    }

    private void executeSqlStatements(Set<String> sqlStatements) throws Exception {
        DataSource dataSource = dataSource();

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            for (String sqlStatement : sqlStatements) {
                statement.execute(sqlStatement);
            }

        }
    }
}
