package org.example.homeworks.hw1;

import org.example.homeworks.hw1.enums.TestResult;

public class Test {
    private TestResult result; // тип результата
    private String testName; // название теста
    private Throwable exception; // упавшее исключение

    public Test(TestResult result, String testName, Throwable exception) {
        this.result = result;
        this.testName = testName;
        this.exception = exception;
    }


    public TestResult getResult() {
        return result;
    }

    public String getTestName() {
        return testName;
    }

    public Throwable getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "Test{" +
                "result=" + result +
                ", testName='" + testName + '\'' +
                ", exception=" + exception +
                '}';
    }
}
