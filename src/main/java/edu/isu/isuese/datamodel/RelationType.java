package edu.isu.isuese.datamodel;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public enum RelationType {
    ASSOCIATION(1),
    AGGREGATION(2),
    COMPOSITION(3),
    DEPENDENCY(4),
    USE(5),
    GENERALIZATION(6),
    REALIZATION(7),
    CONTAINMENT(8);

    private final int value;
    private static Map<Integer, RelationType> map = Maps.newHashMap();

    static {
        map.put(1, ASSOCIATION);
        map.put(2, AGGREGATION);
        map.put(3, COMPOSITION);
        map.put(4, DEPENDENCY);
        map.put(5, USE);
        map.put(6, GENERALIZATION);
        map.put(7, REALIZATION);
        map.put(8, CONTAINMENT);
    }

    RelationType(int value) {
        this.value = value;
    }

    public int value() { return this.value; }

    public static RelationType fromValue(int value) {
        return map.get(value);
    }
}
