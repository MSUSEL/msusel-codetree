package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Pattern extends Model {

    public String getPatternKey() { return getString("patternKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addInstance(PatternInstance inst) { add(inst); save(); }

    public void removeInstance(PatternInstance inst) { remove(inst); save(); }

    public List<PatternInstance> getInstances() { return getAll(PatternInstance.class); }

    public void addRole(Role role) { add(role); save(); }

    public void removeRole(Role role) { remove(role); save(); }

    public List<Role> getRoles() { return getAll(Role.class); }
}
