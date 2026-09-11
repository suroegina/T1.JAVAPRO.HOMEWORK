package org.example.homeworks.hw1;

import org.example.homeworks.hw1.enums.TestResult;

import java.util.List;
import java.util.Map;

public class HomeworkOne {
    public static void main(String[] args) {
        Map<TestResult, List<Test>> results = TestRunner.runTest(Tasks.class);
        for (Map.Entry<TestResult, List<Test>> entry : results.entrySet()) {
            System.out.println("Test: " + entry.getKey());
            for (Test test : entry.getValue()){
                System.out.println(" - " + test.getTestName());
                if (test.getException() != null){
                    System.out.println("Провальный тест. " + test.getException().getMessage());
                }
            }
        }
    }
}
