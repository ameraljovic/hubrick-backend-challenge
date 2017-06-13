package com.aljovic.amer.helper;

import java.util.List;

import static java.util.Collections.binarySearch;

public final class BinarySearchHelper {

    private BinarySearchHelper() {}

    public static int searchUpperBound(final List<Integer> list, final Integer key) {
        final int idx = binarySearch(list, key);
        if (idx < 0) {
            return ((idx + 2) * -1);
        }
        return upperEqualIndex(list, idx, list.get(idx));
    }

    public static int searchLowerBound(final List<Integer> list, final Integer key) {
        final int idx = binarySearch(list, key);
        if (idx < 0) {
            return ((idx + 1) * -1);
        }
        return lowerEqualIndex(list, idx, list.get(idx));
    }

    private static int upperEqualIndex(final List<Integer> list, final int idx, final int value) {
        if (idx + 1 > list.size() - 1) {
            return idx;
        }
        if (list.get(idx + 1) == value) {
            return upperEqualIndex(list, idx + 1, value);
        }
        return idx;
    }

    private static int lowerEqualIndex(final List<Integer> list, final int idx, final int value) {
        if (idx - 1 < 0) {
            return idx;
        }
        if (list.get(idx - 1) == value) {
            return lowerEqualIndex(list, idx - 1, value);
        }
        return idx;
    }
}
