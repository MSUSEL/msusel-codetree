package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Class.class, Enum.class, Interface.class})
public class Method extends TypedMember {

    public void addParameter(Parameter param) { add(param); save(); }

    public void removeParameter(Parameter param) { remove(param); save(); }

    public List<Parameter> getParams() { return getAll(Parameter.class); }

    public void setReturnType(TypeRef ref) { setType(ref); }
}
