package edu.isu.isuese.datamodel;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public enum FileType {
    SOURCE(1),
    TEST(2),
    BUILD(3),
    SCM(4),
    CI(5),
    DOC(6),
    CONFIG(7);

    private final int value;
    private static Map<Integer, FileType> map = Maps.newHashMap();

    static {
        map.put(1, SOURCE);
        map.put(2, TEST);
        map.put(3, BUILD);
        map.put(4, SCM);
        map.put(5, CI);
        map.put(6, DOC);
        map.put(7, CONFIG);
    }

    FileType(int value) { this.value = value; }

    public int value() { return this.value; }

    public static FileType fromValue(int value) { return map.get(value); }
}
