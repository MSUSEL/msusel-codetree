package edu.isu.isuese.datamodel;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public enum TypeRefType {
    WildCard(1),
    TypeVar(2),
    Primitive(3),
    Type(4);

    private final int value;
    private static Map<Integer, TypeRefType> map = Maps.newHashMap();

    static {
        map.put(1, WildCard);
        map.put(2, TypeVar);
        map.put(3, Primitive);
        map.put(4, Type);
    }

    TypeRefType(int value) {this.value = value; }

    public int value() { return this.value; }

    public static TypeRefType fromValue(int value) { return map.get(value); }
}
