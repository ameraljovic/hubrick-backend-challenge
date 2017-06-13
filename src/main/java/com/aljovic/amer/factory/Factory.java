package com.aljovic.amer.factory;

import com.aljovic.amer.service.Processor;
import com.aljovic.amer.service.io.IOService;
import com.aljovic.amer.validation.InputValidator;
import com.aljovic.amer.service.statistics.AverageStatisticsService;
import com.aljovic.amer.service.statistics.StatisticsService;

public final class Factory {

    private Factory() {}

    public static Processor getProcessor() {
        return new Processor(getStatisticsService(), getIOService());
    }

    public static InputValidator getInputValidator() {
        return new InputValidator();
    }

    private static AverageStatisticsService getAverageStatisticsService() {
        return new AverageStatisticsService();
    }

    private static StatisticsService getStatisticsService() {
        return new StatisticsService(getAverageStatisticsService());
    }

    private static IOService getIOService() {
        return new IOService();
    }
}
