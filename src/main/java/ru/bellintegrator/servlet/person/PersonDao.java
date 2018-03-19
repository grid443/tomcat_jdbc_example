package ru.bellintegrator.servlet.person;

import java.util.List;

/**
 * DAO for person objects
 */
public interface PersonDao {

    /**
     * Load all persons
     *
     * @return all persons list
     */
    List<PersonView> all();

    /**
     * Add new person
     *
     * @param personView new person data
     */
    void add(PersonView personView);
}
