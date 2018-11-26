package edu.isu.isuese.datamodel;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public enum Priority {
    VERY_LOW(1),
    LOW(2),
    MODERATE(3),
    HIGH(4),
    VERY_HIGH(5);

    private final int value;
    private static Map<Integer, Priority> map = Maps.newHashMap();

    static {
        map.put(1, VERY_LOW);
        map.put(2, LOW);
        map.put(3, MODERATE);
        map.put(4, HIGH);
        map.put(5, VERY_HIGH);
    }

    Priority(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    static Priority fromValue(int value) {
        return map.get(value);
    }
}
