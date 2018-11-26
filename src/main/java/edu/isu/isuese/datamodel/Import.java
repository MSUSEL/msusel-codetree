package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Import extends Model {

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }
}
