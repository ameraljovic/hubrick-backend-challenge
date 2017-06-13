package com.aljovic.amer;

import com.aljovic.amer.exception.FileNotParsableException;
import com.aljovic.amer.factory.Factory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.aljovic.amer.configuration.Constants.REPORTS_PATH;
import static com.aljovic.amer.helper.PrintHelper.printError;
import static com.aljovic.amer.helper.PrintHelper.printInfo;
import static java.lang.String.format;

public final class Application
{
    private Application() {}

    public static void main(final String[] args ) {
        try {
            if (args.length < 1) {
                printError("No data path supplied. Exiting program");
                System.exit(-1);
            }
            final Path rootPath = Paths.get(args[0]);

            Factory.getInputValidator().validate(rootPath);

            printInfo("Generating reports");
            final long startTime = System.nanoTime();

            Factory.getProcessor().process(rootPath);

            final long endTime = System.nanoTime();
            double runningTime = (endTime - startTime) / 1_000_000.0;

            printInfo(format("\nProgram took %.2f ms", runningTime));
            printInfo(format("Reports generated at %s/", REPORTS_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileNotParsableException e) {
            printError(format("Could not parse any line in %s. Exiting program", e.getFileName()));
            System.exit(-1);
        }
    }
}