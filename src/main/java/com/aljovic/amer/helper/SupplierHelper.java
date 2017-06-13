package com.aljovic.amer.helper;

import java.util.function.Supplier;

import static com.aljovic.amer.helper.PrintHelper.printInfo;
import static java.lang.String.format;

@FunctionalInterface
public interface SupplierHelper<T, E extends Exception> {
    T get() throws E;

    static <T, E extends Exception> Supplier<T> wrapException(SupplierHelper<T, E> function) {
        return () -> {
            try {
                return function.get();
            }
            catch (Exception exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    @SuppressWarnings ("unchecked")
    static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E {
        throw (E)exception;
    }


    static <T> T measureExecutionTime(final Supplier<T> supplier, final String message) {
        final long startTime = System.nanoTime();

        final T result = supplier.get();

        final long endTime = System.nanoTime();
        double runningTime = (endTime - startTime) / 1_000_000.0;

        printInfo(format(message + " took %.2f ms", runningTime));

        return result;
    }
}