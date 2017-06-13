package com.aljovic.amer.mapper;

import com.aljovic.amer.domain.keyvalue.IKeyValue;
import com.aljovic.amer.domain.keyvalue.KeyValue;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class ObjectMapper {

    private ObjectMapper() {}

    public static <T extends IKeyValue> List<KeyValue> toKeyValue(final List<T> list) {
        return list.stream().map(ObjectMapper::toKeyValue).collect(toList());
    }

    private static KeyValue toKeyValue(final IKeyValue keyValue) {
        return KeyValue.of(keyValue.getKey(), keyValue.getValue());
    }
}
