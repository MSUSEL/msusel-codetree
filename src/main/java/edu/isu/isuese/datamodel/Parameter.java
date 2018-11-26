package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Method.class, Constructor.class, Destructor.class})
public class Parameter extends Model {

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addModifier(String mod) { add(Modifier.findFirst("name = ?", mod)); save(); }

    public void removeModifier(String mod) { remove(Modifier.findFirst("name = ?", mod)); save(); }

    public List<Modifier> getModifiers() { return getAll(Modifier.class); }

    public void setType(TypeRef ref) {
        if (this.getAll(TypeRef.class).size() == 0) {
            add(ref);
            save();
        }
        else {
            List<TypeRef> refs = this.getAll(TypeRef.class);
            for (TypeRef r : refs)
                remove(r);

            add(ref);
            save();
        }
    }
}
