package org.example.homeworks.hw1.Errors;

public class TestAssertionError extends Error{
    public TestAssertionError(String errorMessage) {
        super(errorMessage);
    }
}
