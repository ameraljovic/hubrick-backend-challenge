package com.aljovic.amer.service.statistics;

import com.aljovic.amer.domain.report.range.Range;
import com.aljovic.amer.domain.keyvalue.KeyValue;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsService {

    private final AverageStatisticsService averageStatisticsService;

    public StatisticsService(AverageStatisticsService averageStatisticsService) {
        this.averageStatisticsService = averageStatisticsService;
    }

    /**
     * Computes the median value from a list of values. 
     *
     * The list must be sorted into ascending order prior to making this call.
     * If it is not sorted, the results are undefined.
     * 
     * @param values sorted list of values from which to compute the median value
     * @return returns the median value
     */
    public BigDecimal median(final List<BigDecimal> values) {
        int middle = values.size() / 2;
        if (values.size() % 2 == 1) {
            return values.get(middle);
        }
        return values.get(middle - 1).add(values.get(middle))
                .divide(BigDecimal.valueOf(2));
    }

    /**
     * Computes nth percentile for given list
     *
     * The list must be sorted into ascending order prior to making this call.
     * If it is not sorted, the results are undefined.
     *
     * @param percentile percentile from which to computes the result
     * @param values values for which to compute the percentile
     * @return List of elements above given percentile
     */
    public <T> List<T> percentilesOf(final int percentile, final List<T> values) {
        final Double position = (percentile / 100.0) * values.size();
        return values.subList(position.intValue(), values.size());
    }

    /**
     * Computes average values grouped by ranges
     *
     * The list must be sorted into ascending order prior to making this call.
     * If it is not sorted, the results are undefined.
     *
     * @param data List of <{@code key}, {@code value}> pairs
     *             {@code key} is the data structure from which the ranges are computed
     *             {@code value} is the data structure from which the average is computed
     * @param factor Splits the values into n {@code factor} ranges
     * @return list of average values grouped by range based on keys and specified factor
     */
    public List<Range> average(final List<KeyValue<Integer, BigDecimal>> data, int factor) {
        return averageStatisticsService.calculateAverages(data, factor);
    }
}
