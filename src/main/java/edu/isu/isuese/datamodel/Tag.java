package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Tag extends Model {

    public String getTag() { return getString("tag"); }

    public void setTag(String tag) { set("tag", tag); save(); }
}
