package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Rule extends Model {

    public void setPriority(Priority p) {
        set("priority", p.value());
        save();
    }

    public Priority getPriority() {
        int p = (Integer) get("priority");
        return Priority.fromValue(p);
    }
}
