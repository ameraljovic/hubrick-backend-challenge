package com.aljovic.amer.helper;

import static com.aljovic.amer.configuration.Constants.BLACK;
import static com.aljovic.amer.configuration.Constants.GREEN;
import static com.aljovic.amer.configuration.Constants.RED;

public final class PrintHelper {

    private PrintHelper() {}

    public static void printInfo(final String message) {
        System.out.println(GREEN + message + BLACK);
    }

    public static void printError(final String errorMessage) {
        System.err.println(RED + errorMessage + BLACK);
    }
}
