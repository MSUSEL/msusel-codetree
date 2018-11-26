package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("refs")
@BelongsToPolymorphic(parents = {Relation.class, Project.class, Finding.class, Measure.class, RoleBinding.class, TypeRef.class})
public class Reference extends Model {

    public String getRefKey() { return getString("refKey"); }

    public RefType getType() { return RefType.fromValue(getInteger("type")); }

    public void setType(RefType type) { set("type", type.value()); save(); }
}
