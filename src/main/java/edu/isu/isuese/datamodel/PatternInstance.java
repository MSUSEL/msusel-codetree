package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Pattern.class, PatternChain.class, Project.class})
public class PatternInstance extends Model implements Measureable {

    public String getInstKey() { return getString("instKey"); }

    public void addRoleBinding(RoleBinding binding) { add(binding); save(); }

    public List<RoleBinding> getRoleBindings() { return getAll(RoleBinding.class); }

    public void removeRoleBinding(RoleBinding binding) { remove(binding); save(); }
}
