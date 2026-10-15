package org.example.homeworks.hw1;

import org.example.homeworks.hw1.Errors.BadTestClassError;
import org.example.homeworks.hw1.Annotations.*;
import org.example.homeworks.hw1.Errors.TestAssertionError;
import org.example.homeworks.hw1.enums.TestResult;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TestRunner {
    public static Map<TestResult, List<Test>> runTest(Class<?> c) {
        Map<TestResult, List<Test>> resultsTest = new HashMap<>();
        for (TestResult result : TestResult.values()) {
            resultsTest.put(result, new ArrayList<>());
        }
        Object testInstance;

        try {
            testInstance = c.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BadTestClassError("Невозможно создать объект у класса " + c.getName());
        }


        Method beforeSuite = null;
        Method afterSuite = null;
        List<Method> beforeEachMethods = new ArrayList<>();
        List<Method> afterEachMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("@BeforeSuite должен быть статическим методом: " + method.getName());
                }
                beforeSuite = method;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("@AfterSuite должен быть статическим методом: " + method.getName());
                }
                afterSuite = method;
            } else if (method.isAnnotationPresent(BeforeEach.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("@BeforeEach не должен быть статическим методом: " + method.getName());
                }
                beforeEachMethods.add(method);
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("@AfterEach не должен быть статическим методом: " + method.getName());
                }
                afterEachMethods.add(method);
            } else if (method.isAnnotationPresent(org.example.homeworks.hw1.Annotations.Test.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    throw new BadTestClassError("@TestAnnotation не должен быть статическим методом: " + method.getName());
                }
                testMethods.add(method);
            }
        }
        // сортировка @Order по параметру int
        List<Method> sortedOrder = testMethods.stream().sorted(Comparator.comparingInt(method ->{
            Order order = method.getAnnotation(Order.class);
            return (order != null) ? order.value() : 0;
        })).toList();
       /*
        System.out.println("Сортировка @Order");
        for (Method curMethod: sortedOrder) {
            System.out.println(getTestName(curMethod) + " - " + curMethod.getAnnotation(Order.class).value());
        }
        // сортировка @Test по приоритету
        List<Method> sortedTest = sortedOrder.stream().sorted(Comparator.comparingInt(method ->{
            org.example.homeworks.task1.Annotations.Test test = method.getAnnotation(org.example.homewoks.task1.Annotations.Test.class);
            return (test != null) ? test.priority(): 0;
        })).toList();
        testMethods = sortedTest;
        System.out.println("Сортировка @Test");
        for (Method curMethod: sortedTest) {
            System.out.println(getTestName(curMethod) + " - " + curMethod.getAnnotation(org.example.homewoks.task1.Annotations.Test.class).priority());
        }
        */
        if (beforeSuite != null) {
            try {
                beforeSuite.invoke(null);
            } catch (Exception e) {
                throw new BadTestClassError("Ошибка в @BeforeSuite: " + e.getMessage());
            }
        }
        for (Method testMethod : testMethods){
            String testName = getTestName(testMethod);
            boolean disabled = testMethod.isAnnotationPresent(Disabled.class);
            if (disabled){
                resultsTest.get(TestResult.Skipped).add(new Test(TestResult.Skipped, testName, null));
                continue;
            }
            try {
                for (Method before : beforeEachMethods){
                    before.invoke(testInstance);
                }
                testMethod.invoke(testInstance);
                resultsTest.get(TestResult.Succes).add(new Test(TestResult.Succes, testName, null));
            } catch (TestAssertionError e) {
                resultsTest.get(TestResult.Filed).add(new Test(TestResult.Filed, testName, e));
            } catch (Exception e) {
                resultsTest.get(TestResult.Error).add(new Test(TestResult.Error, testName, e));
            } finally {
                for (Method after : afterEachMethods){
                    try {
                        after.invoke(testInstance);
                    } catch (Exception e) {
                        System.out.println("AfterEachMethods Error: " + e.getMessage());
                    }
                }
            }
        }
        if (afterSuite != null) {
            try {
                afterSuite.invoke(null);
            } catch (Exception e) {
                throw new BadTestClassError("Ошибка в @AfterSuite: " + e.getMessage());
            }
        }
        return resultsTest;
    }

    private static String getTestName(Method method){
        org.example.homeworks.hw1.Annotations.Test testAnnotation = method.getAnnotation(org.example.homeworks.hw1.Annotations.Test.class);
        if (testAnnotation != null && !testAnnotation.methodName().isEmpty()){
            return testAnnotation.methodName();
        }
        return method.getName();
    }
}
