package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

public class MethodException extends Model {

    public void removeTypeRef(TypeRef ref) {
        if (ref != null) {
            remove(ref);
            save();
        }
    }

    public void setTypeRef(TypeRef ref) {
        if (ref != null) {
            TypeRef rem = getTypeRef();
            if (rem != null) {
                removeTypeRef(rem);
            }
            add(ref);
        }
    }

    public TypeRef getTypeRef() {
        try {
            return getAll(TypeRef.class).get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
