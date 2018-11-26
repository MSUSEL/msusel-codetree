package edu.isu.isuese.datamodel;

import com.google.common.collect.Lists;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Project.class, Rule.class})
public class Finding extends Model {

    public String getFindingKey() { return getString("findingKey"); }

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
