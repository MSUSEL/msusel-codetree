package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class PatternRepository extends Model {

    public String getRepoKey() { return getString("repoKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addPattern(Pattern pattern) { add(pattern); save(); }

    public void removePattern(Pattern pattern) { remove(pattern); save(); }

    public List<Pattern> getPatterns() { return getAll(Pattern.class); }
}
