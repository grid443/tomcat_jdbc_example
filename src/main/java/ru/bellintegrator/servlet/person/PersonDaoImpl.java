package ru.bellintegrator.servlet.person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class PersonDaoImpl implements PersonDao {
    private static final String LOAD_ALL_QUERY = "SELECT id, first_name, middle_name, last_name, age FROM person";
    private static final String ADD_PERSON_QUERY = "INSERT INTO person " +
            "(first_name, middle_name, last_name, age) " +
            "VALUES " +
            "(?, ?, ?, ?)";

    private final DataSource dataSource;

    public PersonDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PersonView> all() {
        try (Connection conn = dataSource.getConnection(); Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(LOAD_ALL_QUERY);
            return buildPersonViews(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("load persons error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(PersonView personView) {
        if (personView == null) {
            throw new IllegalArgumentException("missed required parameter person");
        }

        personView.validate();

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(ADD_PERSON_QUERY)
        ) {
            statement.setString(1, personView.firstName);
            setOptionalString(statement, 2, personView.middleName);
            statement.setString(3, personView.lastName);
            statement.setLong(4, personView.age);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("save person error", e);
        }
    }

    private List<PersonView> buildPersonViews(ResultSet resultSet) throws SQLException {
        List<PersonView> persons = new ArrayList<>();
        while (resultSet.next()) {
            PersonView person = new PersonView();
            person.id = resultSet.getLong(PersonView.ID);
            person.firstName = resultSet.getString(PersonView.FIRST_NAME);
            person.middleName = resultSet.getString(PersonView.MIDDLE_NAME);
            person.lastName = resultSet.getString(PersonView.LAST_NAME);
            person.age = resultSet.getLong(PersonView.AGE);

            persons.add(person);
        }
        return persons;
    }

    private void setOptionalString(PreparedStatement statement, int parameterIndex, String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            statement.setNull(parameterIndex, Types.VARCHAR);
        } else {
            statement.setString(parameterIndex, value);
        }
    }
}
