package com.aljovic.amer.service.statistics;

import com.aljovic.amer.domain.report.range.Range;
import com.aljovic.amer.domain.report.range.Ranges;
import com.aljovic.amer.domain.keyvalue.KeyValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

import static com.aljovic.amer.helper.BinarySearchHelper.searchLowerBound;
import static com.aljovic.amer.helper.BinarySearchHelper.searchUpperBound;
import static java.util.stream.Collectors.toList;

public class AverageStatisticsService {

    /**
     * Computes average values grouped by a ranges
     * @param data List of <{@code key}, {@code value}> pairs
     *             {@code key} is the data structure from which the ranges are computed
     *             {@code value} is the data structure from which the average is computed
     * @param factor Splits the values into n {@code factor} ranges
     * @return list of average values grouped by range based on keys and specified factor
     */
    List<Range> calculateAverages(final List<KeyValue<Integer, BigDecimal>> data,
                                                     final int factor) {
        return splitToRanges(data.stream().map(KeyValue::getKey).collect(toList()), factor).entrySet().stream()
                .map(range -> {
                    final Integer lowerRange = range.getValue().getLowerBound();
                    final Integer upperRange = range.getValue().getUpperBound();

                    final List<Integer> values = data.stream().map(KeyValue::getKey).collect(toList());

                    int lowerIndex = searchLowerBound(values, lowerRange);
                    int upperIndex = searchUpperBound(values, upperRange);
                    final OptionalDouble averageForKey = data.subList(lowerIndex, upperIndex + 1).stream()
                            .mapToDouble(d -> d.getValue().doubleValue())
                            .average();
                    if (!averageForKey.isPresent()) {
                        return Optional.<Range>empty();
                    }

                    final BigDecimal roundedAverage = BigDecimal.valueOf(averageForKey.getAsDouble())
                            .setScale(2, BigDecimal.ROUND_CEILING);
                    return Optional.of(Range.of(range.getValue(), roundedAverage));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private static Map<Integer, Ranges> splitToRanges(final List<Integer> values, final int factor) {
        final int minValue = values.get(0);
        final int maxValue = values.get(values.size() - 1);
        final int range = (maxValue - minValue) / factor;

        int rangeCount = 0;
        final Map<Integer, Ranges> rangeMap = new HashMap<>();
        List<Integer> currentRange = new ArrayList<>();
        for(int i = minValue; i <= maxValue; i++) {
            currentRange.add(i);
            if (i % range == 0) {
                rangeMap.put(rangeCount, createRange(currentRange));
                currentRange = new ArrayList<>();
                rangeCount++;
            }
        }
        createLastRange(rangeMap, currentRange);
        return rangeMap;
    }

    private static void createLastRange(Map<Integer, Ranges> rangeMap, List<Integer> currentRange) {
        final int lastRangeLowerBound = rangeMap.get(rangeMap.size() - 1).getLowerBound();
        final int lastRangeUpperBound = currentRange.get(currentRange.size() - 1);
        rangeMap.put(rangeMap.size() - 1, Ranges.of(lastRangeLowerBound, lastRangeUpperBound));
    }

    private static Ranges createRange(List<Integer> currentRange) {
        return Ranges.of(currentRange.get(0), currentRange.get(currentRange.size() - 1));
    }
}
