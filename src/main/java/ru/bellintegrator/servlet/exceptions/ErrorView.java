package ru.bellintegrator.servlet.exceptions;

public class ErrorView {

    private final String error;

    public ErrorView(String error) {
        this.error = error;
    }


    public String getError() {
        return error;
    }
}
