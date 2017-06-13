package com.aljovic.amer.domain.report.range;

import com.aljovic.amer.domain.keyvalue.IKeyValue;

import java.math.BigDecimal;

public class Range implements IKeyValue {
    private final Ranges key;
    private final BigDecimal value;

    public static Range of(final Ranges key, final BigDecimal value) {
        return new Range(key, value);
    }

    private Range(final Ranges key, final BigDecimal value) {
        this.key = key;
        this.value = value;
    }

    public Ranges getKey() {
        return key;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Range{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
