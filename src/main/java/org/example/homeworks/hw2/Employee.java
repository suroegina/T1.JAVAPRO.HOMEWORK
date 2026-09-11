package org.example.homeworks.hw2;

public class Employee {
    private String name;
    private int age;
    private String post;

    public Employee(String name, int age, String post) {
        this.name = name;
        this.age = age;
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", post='" + post + '\'' +
                '}';
    }

}
