package edu.isu.isuese.datamodel;

import com.google.common.collect.Lists;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Measure extends Model {

    public String getMeasureKey() { return getString("measureKey"); }

    public double getValue() { return getDouble("value"); }

    public void setValue(double value) { set("value", value); save(); }

    public void setReference(Reference ref) {
        List<Reference> refs = Lists.newArrayList(getReferences());
        for (Reference r : refs) {
            removeReference(r);
        }
        if (ref != null)
            add(ref);
        save();
    }

    public void removeReference(Reference ref) { remove(ref); save(); }

    public List<Reference> getReferences() { return getAll(Reference.class); }
}
