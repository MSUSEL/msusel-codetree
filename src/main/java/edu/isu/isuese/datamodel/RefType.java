package edu.isu.isuese.datamodel;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public enum RefType {
    TYPE(1),
    METHOD(2),
    INITIALIZER(3),
    CONSTRUCTOR(4),
    FIELD(5),
    LITERAL(6);

    private final int value;
    public static Map<Integer, RefType> map = Maps.newHashMap();

    static {
        map.put(1, TYPE);
        map.put(2, METHOD);
        map.put(3, INITIALIZER);
        map.put(4, CONSTRUCTOR);
        map.put(5, FIELD);
        map.put(6, LITERAL);
    }

    RefType(int value) {
        this.value = value;
    }

    public int value() { return value; }

    public static RefType fromValue(int value) { return map.get(value); }
}
