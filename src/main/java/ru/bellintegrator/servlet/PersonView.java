package ru.bellintegrator.servlet;

public class PersonView {

    public String name;

    public int age;

    @Override
    public String toString() {
        return "name:" + name + ";age:" + age;
    }
}
