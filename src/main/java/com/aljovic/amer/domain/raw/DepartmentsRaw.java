package com.aljovic.amer.domain.raw;

public class DepartmentsRaw {
    private final long position;
    private final String name;

    public static DepartmentsRaw of(final long position, final String name) {
        return new DepartmentsRaw(position, name);
    }

    private DepartmentsRaw(final long position, final String name) {
        this.position = position;
        this.name = name;
    }

    public long getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DepartmentsRaw{" +
                "position=" + position +
                ", name='" + name + '\'' +
                '}';
    }
}
