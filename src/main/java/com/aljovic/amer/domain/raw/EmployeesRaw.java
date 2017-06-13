package com.aljovic.amer.domain.raw;

import java.math.BigDecimal;

public class EmployeesRaw {
    private final String department;
    private final String name;
    private final BigDecimal salary;

    public static EmployeesRaw of(final String department, final String name, final BigDecimal salary) {
        return new EmployeesRaw(department, name, salary);
    }

    private EmployeesRaw(final String department, final String name, final BigDecimal salary) {
        this.department = department;
        this.name = name;
        this.salary = salary;
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

    @Override
    public String toString() {
        return "EmployeesRaw{" +
                "department='" + department + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
