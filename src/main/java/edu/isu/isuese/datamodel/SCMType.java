package edu.isu.isuese.datamodel;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public enum SCMType {
    GIT(1),
    SVN(2),
    CVS(3),
    HG(4),
    BAZAAR(5);

    private final int value;
    private static Map<Integer, SCMType> map = Maps.newHashMap();

    static {
        map.put(1, GIT);
        map.put(2, SVN);
        map.put(3, CVS);
        map.put(4, HG);
        map.put(5, BAZAAR);
    }

    SCMType(int value) { this.value = value; }

    public int value() { return value; }

    public static SCMType fromValue(int value) { return map.get(value); }
}
