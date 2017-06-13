package com.aljovic.amer.domain.keyvalue;

public class KeyValue<Key, Value> implements IKeyValue {

    private final Key key;
    private final Value value;

    private KeyValue(final Key key, final Value value) {
        this.key = key;
        this.value = value;
    }

    public static <Key, Value> KeyValue<Key, Value> of(final Key key, final Value value) {
        return new KeyValue<>(key, value);
    }

    public Key getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
