package com.aljovic.amer.validation;

import java.math.BigDecimal;
import java.util.function.Supplier;

final class NumberValidator {

    private NumberValidator() {}

    static boolean isNumeric(final String s) {
        return isTrueIf(() -> new BigDecimal(s)) && isPositive(new BigDecimal(s));
    }

    static boolean isInteger(final String s) {
        return isTrueIf(() -> Integer.valueOf(s)) && isPositive(Integer.valueOf(s));
    }

    private static boolean isPositive(final Number number) {
        return number.doubleValue() >= 0;
    }

    private static <T> boolean isTrueIf(final Supplier<T> supplier) {
        try {
            supplier.get();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
