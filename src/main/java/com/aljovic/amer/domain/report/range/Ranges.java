package com.aljovic.amer.domain.report.range;

import static java.lang.String.format;

public class Ranges {
    private final int lowerBound;
    private final int upperBound;

    public static Ranges of(final int lowerBound, final int upperBound) {
        return new Ranges(lowerBound, upperBound);
    }

    private Ranges(final int lowerBound, final int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    @Override
    public String toString() {
        return format("[%s, %s]", lowerBound, upperBound);
    }
}
