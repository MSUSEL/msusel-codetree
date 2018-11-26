package edu.isu.isuese.datamodel;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public enum Accessibility {
    PUBLIC(1),
    PROTECTED(2),
    PRIVATE(3),
    DEFAULT(4),
    PACKAGE(5),
    INTERNAL(6),
    PROTECTED_INTERNAL(7);

    private final int value;
    private static Map<Integer, Accessibility> map = Maps.newHashMap();

    static {
        map.put(1, PUBLIC);
        map.put(2, PROTECTED);
        map.put(3, PRIVATE);
        map.put(4, DEFAULT);
        map.put(5, PACKAGE);
        map.put(6, INTERNAL);
        map.put(7, PROTECTED_INTERNAL);
    }

    Accessibility(int value) { this.value = value; }

    public int value() { return value; }

    public static Accessibility fromValue(int value) {
        return map.get(value);
    }
}