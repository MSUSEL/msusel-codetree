package edu.isu.isuese.datamodel;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class TypedMember extends Member {

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
