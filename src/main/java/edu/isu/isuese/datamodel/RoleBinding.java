package edu.isu.isuese.datamodel;

import com.google.common.collect.Lists;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class RoleBinding extends Model {

    public Role getRole() { return getAll(Role.class).get(0); }

    private List<Role> getRoles() { return Lists.newLinkedList(getAll(Role.class)); }

    private List<Reference> getRefs() { return Lists.newLinkedList(getAll(Reference.class)); }

    public void setRoleRefPair(Role role, Reference ref) {
        List<Role> roles = getRoles();
        if (!roles.isEmpty()) {
            for (Role r : roles) {
                remove(r);
            }
        }

        List<Reference> refs = getRefs();
        if (!refs.isEmpty()) {
            for (Reference r : refs) {
                remove(r);
            }

        }

        add(role);
        add(ref);
        save();
    }

    public Reference getReference() { return Reference.findById(getInteger("reference_id")); }
}
