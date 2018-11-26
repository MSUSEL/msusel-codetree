package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class PatternChain extends Model {

    public String getChainKey() { return getString("chainKey"); }

    public void addInstance(PatternInstance inst) { add(inst); save(); }

    public void removeInstance(PatternInstance inst) { remove(inst); save(); }

    public List<PatternInstance> getInstances() { return getAll(PatternInstance.class); }
}
