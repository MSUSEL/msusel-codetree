package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class RuleRepository extends Model {

    public String getRepoKey() { return getString("repoKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addRule(Rule rule) { add(rule); save(); }

    public void removeRule(Rule rule) { remove(rule); save(); }

    public List<Rule> getRules() { return getAll(Rule.class); }
}
