package ru.bellintegrator.servlet.config;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
        log.info(String.format("DataBase schema initialization start. %s", schemaFileLocation));

        try {
            Set<String> sqlStatements = readSqlStatements();
            executeSqlStatements(sqlStatements);

            log.info(String.format("Database schema initialized. %s", schemaFileLocation));
        } catch (Exception e) {
            throw new RuntimeException("Database schema initialization error", e);
        }
    }

    private Set<String> readSqlStatements() throws Exception {
        StringBuilder statementsBuilder = new StringBuilder();
        Set<String> sqlStatements = new HashSet<>();

        log.info(String.format("reading file %s start", schemaFileLocation));

        try (InputStream sqlSchemaStream = DataBaseInitializer.class.getClassLoader().getResourceAsStream(schemaFileLocation)) {
            if (sqlSchemaStream == null) {
                throw new FileNotFoundException("Schema initialization file not found in location: " + schemaFileLocation);
            }
            try (
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    sqlSchemaStream,
                                    DefaultCharsetFilter.DEFAULT_CHARSET
                            )
                    )
            ) {
                reader.lines().forEach(
                        line -> {
                            if (line.isEmpty() && statementsBuilder.length() > 0) {
                                String sqlStatement = buildSqlStatement(statementsBuilder);
                                sqlStatements.add(sqlStatement);
                                statementsBuilder.setLength(0);
                                return;
                            }

                            statementsBuilder.append(line);
                            statementsBuilder.append(" ");
                        }
                );
            }
        }

        if (statementsBuilder.length() > 0) {
            String sqlStatement = buildSqlStatement(statementsBuilder);
            sqlStatements.add(sqlStatement);
        }

        log.info(String.format("reading file %s success", schemaFileLocation));

        return sqlStatements;
    }

    private String buildSqlStatement(StringBuilder statementsBuilder) {
        String statement = statementsBuilder.toString().trim();

        if (statement.endsWith(";")) {
            statement = statement.substring(0, statement.length() - 1);
        }

        return statement;
    }

    private DataSource dataSource() throws Exception {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup(ApplicationConfig.CONTEXT_NAME);
        return (DataSource) envContext.lookup(ApplicationConfig.H2_DATA_SOURCE_NAME);
    }

    private void executeSqlStatements(Set<String> sqlStatements) throws Exception {
        DataSource dataSource = dataSource();

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            try {
                connection.setAutoCommit(false);
                for (String sqlStatement : sqlStatements) {
                    statement.execute(sqlStatement);
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        }
    }
}
