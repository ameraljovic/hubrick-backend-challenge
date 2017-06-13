package com.aljovic.amer.service;

import com.aljovic.amer.domain.Employee;
import com.aljovic.amer.domain.report.Median;
import com.aljovic.amer.domain.report.Percentile;
import com.aljovic.amer.domain.report.range.Range;
import com.aljovic.amer.service.io.IOService;
import com.aljovic.amer.service.statistics.StatisticsService;
import com.aljovic.amer.domain.keyvalue.KeyValue;
import com.aljovic.amer.helper.RunnableHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.aljovic.amer.configuration.Constants.AGES;
import static com.aljovic.amer.configuration.Constants.AGE_BY_DEPARTMENT_PATH;
import static com.aljovic.amer.configuration.Constants.AVERAGE_INCOME_BY_AGE_RANGE_PATH;
import static com.aljovic.amer.configuration.Constants.DEPARTMENTS;
import static com.aljovic.amer.configuration.Constants.EMPLOYEES;
import static com.aljovic.amer.configuration.Constants.INCOME_BY_DEPARTMENT_PATH;
import static com.aljovic.amer.configuration.Constants.PERCENTILE_INCOME_BY_DEPARTMENT_PATH;
import static com.aljovic.amer.mapper.ObjectMapper.toKeyValue;
import static com.aljovic.amer.helper.SupplierHelper.measureExecutionTime;
import static com.aljovic.amer.helper.SupplierHelper.wrapException;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Processor {

    private final StatisticsService statisticsService;
    private final IOService ioService;

    public Processor(final StatisticsService statisticsService, final IOService ioService) {
        this.statisticsService = statisticsService;
        this.ioService = ioService;
    }

    public void process(final Path path) throws IOException {

        final List<Employee> employees = measureExecutionTime(
                wrapException(() -> ioService.readEmployees(
                        path.resolve(DEPARTMENTS), path.resolve(EMPLOYEES), path.resolve(AGES))),
                "Reading and merging files");

        final List<Employee> employeesSortByAge = measureExecutionTime(() ->
                        employees.stream().sorted(comparing(Employee::getAge)).collect(toList()),
                "Sorting employees by age");

        final List<Employee> employeesSortBySalary = measureExecutionTime(() ->
                        employees.stream().sorted(comparing(Employee::getSalary)).collect(toList()),
                "Sorting employees by salary");

        final List<Median> medianIncomeByDepartment = measureExecutionTime(() ->
                medianIncomeByDepartment(employeesSortBySalary),
                "Calculating median income by department");

        final List<Median> medianAgeByDepartment = measureExecutionTime(() ->
                medianAgeByDepartment(employeesSortByAge),
                "Calculating median age by department");


        final List<Percentile> incomeByDepartment95thPercentile = measureExecutionTime(() ->
                percentileByDepartment(medianIncomeByDepartment),
                "Calculating 95th percentile department incomes");

        final List<Range> averageIncomeByAgeRanges = measureExecutionTime(() ->
                averageIncomeByAgeRange(employeesSortByAge),
                "Calculating average income by age range with factor of 10");

        RunnableHelper.measureExecutionTime(() -> {
            try {
                ioService.writeReport(AGE_BY_DEPARTMENT_PATH, toKeyValue(medianAgeByDepartment), KeyValue.of("department", "median age"));
                ioService.writeReport(INCOME_BY_DEPARTMENT_PATH, toKeyValue(medianIncomeByDepartment), KeyValue.of("department", "median income"));
                ioService.writeReport(PERCENTILE_INCOME_BY_DEPARTMENT_PATH, toKeyValue(incomeByDepartment95thPercentile),
                        KeyValue.of("department", "95-percentile income"));
                ioService.writeReport(AVERAGE_INCOME_BY_AGE_RANGE_PATH, toKeyValue(averageIncomeByAgeRanges), KeyValue.of("average income", "age range"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Writing reports");
    }

    private List<Range> averageIncomeByAgeRange(List<Employee> employeesSortByAge) {
        final List<KeyValue<Integer, BigDecimal>> salariesAndAges = employeesSortByAge.stream()
                .map(employee -> KeyValue.of(employee.getAge(), employee.getSalary()))
                .collect(toList());
        return statisticsService.average(salariesAndAges, 10);
    }

    private List<Median> medianAgeByDepartment(List<Employee> employeesSortByAge) {
        final Map<String, List<Employee>> ageByDepartment = employeesSortByAge.stream()
                .collect(groupingBy(Employee::getDepartment, toList()));

        return ageByDepartment.entrySet().stream()
                .map(department -> {
                    final List<BigDecimal> ages = department.getValue().stream()
                            .map(Employee::getAge)
                            .map(BigDecimal::valueOf)
                            .collect(toList());
                    return Median.of(department.getKey(), statisticsService.median(ages));
                }).collect(toList());
    }

    private List<Median> medianIncomeByDepartment(List<Employee> employeesSortBySalary) {
        final Map<String, List<Employee>> incomeByDepartment = employeesSortBySalary.stream()
                .collect(groupingBy(Employee::getDepartment, toList()));
        return incomeByDepartment.entrySet().stream()
                .map(department -> {
                    final List<BigDecimal> salaries = department.getValue().stream()
                            .map(Employee::getSalary)
                            .collect(toList());
                    return Median.of(department.getKey(), statisticsService.median(salaries));
                }).collect(toList());
    }

    private List<Percentile> percentileByDepartment(List<Median> medianIncomeByDepartment) {
        final List<Median> sortedMedianIncome = medianIncomeByDepartment.stream()
                .sorted(comparing(Median::getValue))
                .collect(toList());

        return statisticsService.percentilesOf(95, sortedMedianIncome).stream()
                .map(median -> Percentile.of(median.getKey(), median.getValue()))
                .collect(toList());
    }
}
