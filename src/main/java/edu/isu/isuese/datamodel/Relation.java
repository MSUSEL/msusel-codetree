package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Relation extends Model {

    public String getRelKey() {
        return getString("relKey");
    }

    public void setToAndFromRefs(Reference to, Reference from) {
        setToReference(to);
        setFromReference(from);
    }

    public void setToReference(Reference ref) {
        int value = 0;
        if (get("to_id") != null)
            value = getInteger("to_id");
        if (value > 0) {
            removeReference(Reference.findById(value));
        }
        if (ref == null)
            set("to_id", 0);
        else {
            addReference(ref);
            set("to_id", ref.getId());
        }
        save();
    }

    public void setFromReference(Reference ref) {
        int value = 0;
        if (get("from_id") != null)
            value = getInteger("from_id");
        if (value > 0) {
            removeReference(Reference.findById(value));
        }
        if (ref == null)
            set("from_id", 0);
        else {
            addReference(ref);
            set("from_id", ref.getId());
        }
        save();
    }

    private void addReference(Reference ref) {
        add(ref);
        save();
    }

    private void removeReference(Reference ref) {
        remove(ref);
        save();
    }

    public RelationType getType() {
        return RelationType.fromValue(getInteger("type"));
    }

    public void setType(RelationType type) {
        set("type", type.value());
        save();
    }

    public List<Reference> getReferences() {
        return getAll(Reference.class);
    }
}
