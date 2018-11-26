package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class Component extends Model implements Measureable {

    public void setStart(int start) { set("start", start); save(); }

    public int getStart() { return getInteger("end"); }

    public void setEnd(int end) { set("end", end); save(); }

    public int getEnd() { return getInteger("end"); }

    public void setName(String name) { set("name", name); save(); }

    public String getName() { return getString("name"); }

    public void setAccessibility(Accessibility access) { set("accessibility", access.value()); save(); }

    public Accessibility getAccessibility() { return Accessibility.fromValue(getInteger("accessibility")); }

    public void addModifier(String mod) { add(Modifier.findFirst("name = ?", mod)); save(); }

    public void removeModifier(String mod) { remove(Modifier.findFirst("name = ?", mod)); save(); }

    public List<Modifier> getModifiers() { return getAll(Modifier.class); }
}
