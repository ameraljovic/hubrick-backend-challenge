package com.aljovic.amer.domain.raw;

public class AgesRaw {
    private final String name;
    private final Integer age;

    public static AgesRaw of(final String name, final Integer age) {
        return new AgesRaw(name, age);
    }

    private AgesRaw(final String name, final Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "AgesRaw{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
