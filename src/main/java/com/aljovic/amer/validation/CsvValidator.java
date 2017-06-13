package com.aljovic.amer.validation;

import com.aljovic.amer.domain.raw.CsvField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.aljovic.amer.configuration.Constants.AGES;
import static com.aljovic.amer.configuration.Constants.CSV_DELIMITER;
import static com.aljovic.amer.configuration.Constants.EMPLOYEES;
import static com.aljovic.amer.helper.PrintHelper.printError;
import static com.aljovic.amer.validation.NumberValidator.isInteger;
import static com.aljovic.amer.validation.NumberValidator.isNumeric;
import static java.lang.String.format;

public final class CsvValidator {
    private final String fileName;
    private final List<CsvField> fields;

    private static final CsvValidator EMPLOYEES_VALIDATOR = createEmployeeValidator();
    private static final CsvValidator AGES_VALIDATOR = createAgeValidator();

    public static Predicate<String> validateEmployees(final Map<Long, String> departments) {
        return line -> EMPLOYEES_VALIDATOR.validate(line) && departmentExists(line, departments);
    }

    public static Predicate<String> validateAges() {
        return AGES_VALIDATOR::validate;
    }

    private static CsvValidator createEmployeeValidator() {
        final List<CsvField> fields = new ArrayList<>(4);
        fields.add(CsvField.of("departmentPosition", Integer.class));
        fields.add(CsvField.of("name", String.class));
        fields.add(CsvField.of("gender", String.class, true));
        fields.add(CsvField.of("salary", BigDecimal.class));
        return new CsvValidator(EMPLOYEES, fields);
    }

    private static CsvValidator createAgeValidator() {
        final List<CsvField> fields = new ArrayList<>(2);
        fields.add(CsvField.of("name", String.class));
        fields.add(CsvField.of("salary", Integer.class));
        return new CsvValidator(AGES, fields);
    }

    private CsvValidator(final String fileName, final List<CsvField> fields) {
        this.fileName = fileName;
        this.fields = fields;
    }

    public static Predicate<String> isNotEmpty() {
        return line -> !isEmpty(line);
    }

    private boolean validate(final String line) {

        if (isEmpty(line)) {
            printError(format("Could not parse line '%s' from %s because it is empty", line, fileName));
            return false;
        }

        // I'm aware that StringTokenizer is much faster then String.split()
        // but StringTokenizer skips empty tokens. That is the reason I decided
        // to use String.split
        final String[] tokens = line.split(CSV_DELIMITER);

        if (tokens.length < fields.size()) {
            printError(format("Could not parse line '%s' from %s because it has not %d elements separated by '%s'",
                    line, fileName, fields.size(), CSV_DELIMITER));
            return false;
        }

        for(int i = 0; i < fields.size(); i++) {
            final CsvField csvField = fields.get(i);
            final String token = tokens[i];

            if (csvField.isIgnored()) {
                continue;
            }

            if (csvField.getType() == BigDecimal.class) {
                if (!isNumeric(token)) {
                    printError(format("Could not parse line '%s' from %s because field '%s' is not numeric",
                            line, fileName, csvField.getName()));
                    return false;
                }
            }

            if (csvField.getType() == Integer.class) {
                if (!isInteger(token)) {
                    printError(format("Could not parse line '%s' from %s because field '%s' is not an integer",
                            line, fileName, csvField.getName()));
                    return false;
                }
            }

            if (csvField.getType() == String.class) {
                if (isNumeric(token)) {
                    printError(format("Could not parse line '%s' from %s because %s is not supposed to be a number",
                            line, fileName, csvField.getName()));
                    return false;
                }

                if (isEmpty(token)) {
                    printError(format("Could not parse line '%s' from %s because %s is empty", line, fileName, csvField.getName()));
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean departmentExists(final String line, final Map<Long, String> departments) {
        final String[] tokens = line.split(CSV_DELIMITER);
        final String departmentPosition = tokens[0];

        final String department = departments.get(Long.valueOf(departmentPosition));
        if (department == null) {
            printError(format("Could not parse line '%s' from %s because department does not exist for given index",
                    line, EMPLOYEES));
            return false;
        }

        return true;
    }

    private static boolean isEmpty(String token) {
        return token.trim().length() == 0;
    }
}
