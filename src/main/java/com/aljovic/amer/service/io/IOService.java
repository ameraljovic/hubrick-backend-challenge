package com.aljovic.amer.service.io;

import com.aljovic.amer.domain.Employee;
import com.aljovic.amer.domain.keyvalue.KeyValue;
import com.aljovic.amer.domain.raw.AgesRaw;
import com.aljovic.amer.domain.raw.DepartmentsRaw;
import com.aljovic.amer.domain.raw.EmployeesRaw;
import com.aljovic.amer.exception.FileNotParsableException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.aljovic.amer.configuration.Constants.AGES;
import static com.aljovic.amer.configuration.Constants.COMMA;
import static com.aljovic.amer.configuration.Constants.CSV_DELIMITER;
import static com.aljovic.amer.configuration.Constants.DEPARTMENTS;
import static com.aljovic.amer.configuration.Constants.EMPLOYEES;
import static com.aljovic.amer.configuration.Constants.REPORTS_PATH;
import static com.aljovic.amer.helper.PrintHelper.printError;
import static com.aljovic.amer.mapper.CsvMapper.mapAges;
import static com.aljovic.amer.mapper.CsvMapper.mapDepartments;
import static com.aljovic.amer.mapper.CsvMapper.mapEmployees;
import static com.aljovic.amer.validation.CsvValidator.isNotEmpty;
import static com.aljovic.amer.validation.CsvValidator.validateAges;
import static com.aljovic.amer.validation.CsvValidator.validateEmployees;
import static java.lang.String.format;
import static java.util.Collections.binarySearch;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class IOService {

    /**
     *  Reads, validates and maps files to objects
     *
     * @param departmentsPath Path to departments.csv
     * @param employeesPath Path to employees.csv
     * @param agesPath Path to ages.csv
     * @return Representation of employees in {department, name, salary, age} format
     * @throws IOException If an I/O error occurs
     */
    public List<Employee> readEmployees(final Path departmentsPath, final Path employeesPath, final Path agesPath) throws IOException {

        final Map<Long, String> departments = readDepartments(departmentsPath);

        if (departments.isEmpty()) {
            throw new FileNotParsableException(DEPARTMENTS);
        }

        final List<EmployeesRaw> employees = Files.readAllLines(employeesPath).stream()
                .filter(validateEmployees(departments))
                .map(mapEmployees(departments))
                .sorted(comparing(EmployeesRaw::getName))
                .collect(toList());

        if (employees.isEmpty()) {
            throw new FileNotParsableException(EMPLOYEES);
        }

        final List<AgesRaw> ages = Files.readAllLines(agesPath).stream()
                .filter(validateAges())
                .map(mapAges())
                .sorted(comparing(AgesRaw::getName))
                .collect(toList());

        if (ages.isEmpty()) {
            throw new FileNotParsableException(AGES);
        }

        return mergeEmployees(employees, ages);
    }

    /**
     *  Writes csv files with header
     *
     * @param output the path to the output file
     * @param values values to be written to a file
     * @param headers headers which will be written to the first line
     * @throws IOException If an I/O error occurs
     */
    public void writeReport(final Path output, final List<KeyValue> values, final KeyValue<String, String> headers) throws IOException {
        if (!Files.exists(REPORTS_PATH)) {
            Files.createDirectory(REPORTS_PATH);
        }

        try (final BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write(headers.getKey() + COMMA + headers.getValue());
            writer.newLine();
            for (KeyValue value : values) {
                writer.write(value.getKey() + CSV_DELIMITER + value.getValue());
                writer.newLine();
            }
        }
    }

    private static List<Employee> mergeEmployees(final List<EmployeesRaw> employeesRaw, final List<AgesRaw> agesRaw) {
        final List<Employee> mergedEmployees = new ArrayList<>();

        int employeesIdx = 0, agesIdx = 0;
        while (employeesIdx < employeesRaw.size() && agesIdx < agesRaw.size()) {
            final EmployeesRaw employee = employeesRaw.get(employeesIdx);
            final String employeeName = agesRaw.get(agesIdx).getName();
            if (!employeeName.equals(employee.getName())) {
                if (!employeeExists(employee, agesRaw)) {
                    printError(format("Employee '%s' from %s could not be found. Either he does not exist " +
                            "or could not be parsed", employee.getName(), AGES));
                    employeesIdx++;
                    continue;
                }
                printError(format("Could not parse line from %s because employee %s could not be found " +
                        "in validated employee list", AGES, employeeName));
                agesIdx++;
                continue;

            }
            final int age = agesRaw.get(agesIdx).getAge();
            final Employee mergedEmployee = Employee.of(employee.getDepartment(), employee.getName(),
                    employee.getSalary(), age);
            mergedEmployees.add(mergedEmployee);
            employeesIdx++;
            agesIdx++;
        }
        return mergedEmployees;
    }

    private static Map<Long, String> readDepartments(final Path departmentsPath) throws IOException {
        final AtomicLong position = new AtomicLong();
        return Files.readAllLines(departmentsPath).stream()
                .filter(isNotEmpty())
                .map(mapDepartments(position))
                .collect(toMap(DepartmentsRaw::getPosition, DepartmentsRaw::getName));
    }

    private static boolean employeeExists(final EmployeesRaw employee, final List<AgesRaw> ages) {
        return binarySearch(ages.stream().map(AgesRaw::getName).collect(toList()), employee.getName()) >= 0;
    }
}
