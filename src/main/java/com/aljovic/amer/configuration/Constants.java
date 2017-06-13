package com.aljovic.amer.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Constants {

    private Constants() {}

    private static final String USER_DIR = "user.dir";
    private static final String REPORTS = "reports";
    private static final Path ROOT_PATH = Paths.get(System.getProperty(USER_DIR));

    public static final Path REPORTS_PATH = ROOT_PATH.resolve(REPORTS);

    public static final String DEPARTMENTS = "departments.csv";
    public static final String EMPLOYEES = "employees.csv";
    public static final String AGES = "ages.csv";

    public static final Path INCOME_BY_DEPARTMENT_PATH = REPORTS_PATH.resolve("income-by-department.csv");
    public static final Path AGE_BY_DEPARTMENT_PATH = REPORTS_PATH.resolve("employee-age-by-department.csv");
    public static final Path PERCENTILE_INCOME_BY_DEPARTMENT_PATH = REPORTS_PATH.resolve("income-95-by-department.csv");
    public static final Path AVERAGE_INCOME_BY_AGE_RANGE_PATH = REPORTS_PATH.resolve("income-average-by-age-range.csv");

    public static final String CSV_DELIMITER = ",";
    public static final String COMMA = ";";

    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLACK = "\033[0;30m";
}
