package org.example.homeworks.hw2;

import java.util.*;
import java.util.stream.Collectors;

public class HomeworkTwo {
    public static void main(String[] args) {
        System.out.println("\n1. найти 3е наибольшее число");
        int[] numbers = {5,2,10,9,4,3,10,1,13};
        int thirdLargestNumber = Arrays.stream(numbers)
                .boxed()
                .sorted(Collections.reverseOrder())
                .skip(2)
                .findFirst()
                .orElse(1);
        System.out.println("Третье наибольшее число в " + Arrays.toString(numbers) + " - " + thirdLargestNumber);

        System.out.println("\n2. Найти 3-е наибольшее уникальное число");
        int thirdLargestUniqueNumber = Arrays.stream(numbers)
                .boxed()
                .distinct()
                .sorted(Collections.reverseOrder())
                .skip(2)
                .findFirst()
                .orElse(1);
        System.out.println("Третье наибольшее уникальное число в " + Arrays.toString(numbers) + " - " + thirdLargestUniqueNumber);


        List<Employee> employees = Arrays.asList(
                new Employee("Иван", 35, "Программист"),
                new Employee("Владимир", 39, "Инженер"),
                new Employee("Петр", 28, "Инженер"),
                new Employee("Марина", 53, "Менеджер"),
                new Employee("Светлана", 40, "Инженер"),
                new Employee("Анна", 28, "Инженер"),
                new Employee("Степан", 37, "Инженер"),
                new Employee("Ангелина", 36, "Тестировщик")
        );

        System.out.println("\n3. Найти трех самых старших сотрудников с должностью Инженер");
        List<Employee> topOldEngineers = employees
                .stream()
                .filter(employee -> employee.getPost().equalsIgnoreCase("Инженер"))
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .limit(3)
                .toList();
        System.out.println("Первые три старшие сотрудники с должностью Инженер : " + topOldEngineers.toString());

        System.out.println("\n4. Средний возраст среди инженеров");
        double avgAgeEngineers = employees
                .stream()
                .filter(employee -> employee.getPost().equalsIgnoreCase("Инженер"))
                .mapToInt(Employee::getAge)
                .average()
                .orElse(0.0);
        System.out.println("Средний возраст среди инженеров : " + avgAgeEngineers);

        System.out.println("\n5. Найти  списке самое длинное слово");
        String text = "Java Pro — это продвинутый уровень владения языком Java, который включает глубокое знание архитектуры JVM, фреймворка Spring, многопоточности, баз данных и микросервисной инфраструктуры, необходимый для разработки сложных и надежных систем";
        String[] words = text.split("[^\\p{L}\\p{Nd}]+");
        System.out.println("Текст, который надо разбить на слова: " + text);
        //System.out.println(Arrays.toString(words));

        String[] sortWords = Arrays
                .stream(words)
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .toArray(String[]::new);
        System.out.println("Сортировка слов по тексту: " + Arrays.toString(sortWords));


        String longestWord = Arrays.stream(words)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
        System.out.println("Самое длинное слово в тексте: " + longestWord);

        System.out.println("\n6. HashMap : слово - сколько раз втсречается в тексте");
        String wordsList = "Кот писал код на JAVA написал кот плохой код ужасно плохой код но кот хороший";
        System.out.println(wordsList);
        HashMap<String, Long> wordCount = Arrays
                .stream(wordsList.toLowerCase().split(" "))
                .collect(Collectors.groupingBy(word -> word, HashMap::new, Collectors.counting()));
        System.out.println(wordCount);

        System.out.println("\n6. Массив строк, найти самое длинное");
        String[] strings = {
                "код функция стрим строки потоки",
                "геометрия алгебра математика олимпиада анализ",
                "семья папа мама сын дочь",
                "один два три четыре пять"
        };
        System.out.println(Arrays.toString(strings));

        String longestWordInArray = Arrays.stream(strings)
                .flatMap(string -> Arrays.stream(string.split(" ")))
                .max(Comparator.comparingInt(String::length))
                .orElse("");
        System.out.println("Самое длинное слово в массиве строк: " + longestWordInArray);
    }
}
