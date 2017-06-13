package com.aljovic.amer.domain.report;

import com.aljovic.amer.domain.keyvalue.IKeyValue;

import java.math.BigDecimal;

public class Median implements IKeyValue {
    private final String key;
    private final BigDecimal value;

    public static Median of(final String key, final BigDecimal value) {
        return new Median(key, value);
    }

    private Median(final String key, final BigDecimal value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Median{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
