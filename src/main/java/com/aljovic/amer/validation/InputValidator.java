package com.aljovic.amer.validation;

import java.nio.file.Path;

import static com.aljovic.amer.configuration.Constants.AGES;
import static com.aljovic.amer.configuration.Constants.DEPARTMENTS;
import static com.aljovic.amer.configuration.Constants.EMPLOYEES;
import static com.aljovic.amer.helper.PrintHelper.printError;
import static java.lang.String.format;

public class InputValidator {

    public void validate(final Path path) {
        if (!path.toFile().exists()) {
            printError("Supplied directory does not exist. Exiting program");
            System.exit(-1);
        }

        validateFileExists(path, DEPARTMENTS);
        validateFileExists(path, EMPLOYEES);
        validateFileExists(path, AGES);
    }

    private static void validateFileExists(final Path path, final String fileName) {
        if (!path.resolve(fileName).toFile().exists()) {
            printError(format("%s directory does not exist. Exiting program", fileName));
            System.exit(-1);
        }
    }
}
