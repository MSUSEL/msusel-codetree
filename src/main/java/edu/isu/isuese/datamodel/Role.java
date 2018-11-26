package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Role extends Model {

    public String getRoleKey() { return getString("roleKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }
}
