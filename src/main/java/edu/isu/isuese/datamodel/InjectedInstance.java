package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Manies;
import org.javalite.activejdbc.annotations.Many2Many;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;
import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("injected_instances")
@Many2Manies({
        @Many2Many(other = Project.class, join = "projects_injected_instances", sourceFKName = "project_id", targetFKName = "injected_instance_id"),
        @Many2Many(other = Finding.class, join = "findings_injected_instances", sourceFKName = "finding_id", targetFKName = "injected_instance_id")
})
public class InjectedInstance extends Model {

    public InjectedInstance() {
        save();
    }

    public void addReference(Reference ref) {
        if (ref == null)
            return;

        add(ref);
        save();
    }

    public void removeReference(Reference ref) {
        if (ref == null)
            return;

        remove(ref);
        save();
    }

    public List<Reference> getReferences() {
        return getAll(Reference.class);
    }

    public Project getParentProject() {
        return parent(Project.class);
    }

    public Finding getParentFinding() {
        return parent(Finding.class);
    }

    public InjectedInstance copy(String oldPrefix, String newPrefix) {
        InjectedInstance copy = InjectedInstance.create();

        getReferences().forEach(ref -> copy.addReference(ref.copy(oldPrefix, newPrefix)));

        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InjectedInstance) {
            InjectedInstance fd = (InjectedInstance) obj;
            if (fd.getId().equals(getId()))
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
