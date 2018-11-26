package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToParents({
        @BelongsTo(foreignKeyName="module_id", parent=Module.class),
        @BelongsTo(foreignKeyName="namespace_id", parent=Namespace.class)
})
public class Namespace extends Model implements Measureable {

    public String getNsKey() { return getString("nsKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addFile(File file) { add(file); save(); }

    public void removeFile(File file) { remove(file); save(); }

    public List<File> getFiles() { return getAll(File.class); }

    public void addNamespace(Namespace ns) { add(ns); save(); }

    public void removeNamespace(Namespace ns) { remove(ns); save(); }

    public List<Namespace> getNamespaces() { return getAll(Namespace.class); }
}
