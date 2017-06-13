package com.aljovic.amer.helper;

import static com.aljovic.amer.helper.PrintHelper.printInfo;
import static java.lang.String.format;

public interface RunnableHelper {

    static void measureExecutionTime(final Runnable runnable, final String message) {
        final long startTime = System.nanoTime();

        runnable.run();

        final long endTime = System.nanoTime();
        double runningTime = (endTime - startTime) / 1_000_000.0;

        printInfo(format(message + " took %.2f ms", runningTime));
    }
}

