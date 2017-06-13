package com.aljovic.amer.mapper;

import com.aljovic.amer.domain.raw.DepartmentsRaw;
import com.aljovic.amer.domain.raw.EmployeesRaw;
import com.aljovic.amer.domain.raw.AgesRaw;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static com.aljovic.amer.configuration.Constants.CSV_DELIMITER;

public final class CsvMapper {

    private CsvMapper() {}

    public static Function<String, AgesRaw> mapAges() {
        return line -> {
            final String[] tokens = line.split(CSV_DELIMITER);
            return AgesRaw.of(tokens[0], Integer.valueOf(tokens[1]));
        };
    }

    public static Function<String, EmployeesRaw> mapEmployees(final Map<Long, String> departments) {
        return line -> {
            final String[] tokens = line.split(CSV_DELIMITER);

            final long departmentPosition = Long.valueOf(tokens[0]);
            final String name = tokens[1];
            final BigDecimal salary = new BigDecimal(tokens[3]);
            final String department = departments.get(departmentPosition);

            return EmployeesRaw.of(department, name, salary);
        };
    }

    public static Function<String, DepartmentsRaw> mapDepartments(final AtomicLong position) {
        return line -> DepartmentsRaw.of(position.incrementAndGet(), line);
    }
}
