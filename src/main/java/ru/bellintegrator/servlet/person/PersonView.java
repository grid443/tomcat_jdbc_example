package ru.bellintegrator.servlet.person;

public class PersonView {
    public static final String ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String MIDDLE_NAME = "middle_name";
    public static final String LAST_NAME = "last_name";
    public static final String AGE = "age";

    public long id;

    public String firstName;

    public String middleName;

    public String lastName;

    public long age;

    public void validate() {
        if (isNullOrEmpty(firstName)) {
            throw new IllegalStateException("missed required parameter firstName");
        }

        if (isNullOrEmpty(lastName)) {
            throw new IllegalStateException("missed required parameter lastName");
        }

        if (age < 1 || age > 300) {
            throw new IllegalStateException("age value is out of range (1 - 300): " + age);
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
