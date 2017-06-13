package com.aljovic.amer.domain.raw;

public class CsvField {
    private final String name;
    private final Class type;
    private final boolean ignored;

    public static CsvField of(final String name, final Class type, final boolean ignored) {
        return new CsvField(name, type, ignored);
    }

    public static CsvField of(final String name, final Class type) {
        return new CsvField(name, type, false);
    }

    private CsvField(final String name, final Class type, final boolean ignored) {
        this.name = name;
        this.ignored = ignored;
        this.type = type;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public Class getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CsvField{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", ignored=" + ignored +
                '}';
    }
}
