package com.aljovic.amer.domain;

import java.math.BigDecimal;

public class Employee {
    private final String department;
    private final String name;
    private final BigDecimal salary;
    private final int age;

    public static Employee of(final String department, final String name, final BigDecimal salary, final int age) {
        return new Employee(department, name, salary, age);
    }

    private Employee(final String department, final String name, final BigDecimal salary, final int age) {
        this.department = department;
        this.name = name;
        this.salary = salary;
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "department='" + department + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }
}