package edu.isu.isuese.datamodel;

import com.google.common.collect.Lists;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class File extends Model implements Measureable {

    public String getFileKey() {
        return getString("fileKey");
    }

    public void setName(String name) {
        set("name", name);
        save();
    }

    public String getName() {
        return getString("name");
    }

    public FileType getType() {
        if (get("type") == null)
            return null;
        else
            return FileType.fromValue(getInteger("type"));
    }

    public void setType(FileType type) {
        if (type == null)
            set("type", null);
        else
            set("type", type.value());
    }

    public void addImport(Import imp) {
        add(imp);
        save();
    }

    public void removeImport(Import imp) {
        remove(imp);
        save();
    }

    public List<Import> getImports() {
        return getAll(Import.class);
    }

    public void addType(Type t) {
        add(t);
        save();
    }

    public void removeType(Type t) {
        remove(t);
        save();
    }

    public List<Type> getTypes() {
        List<Type> types = Lists.newArrayList();
        types.addAll(getAll(Class.class));
        types.addAll(getAll(Interface.class));
        types.addAll(getAll(Enum.class));
        return types;
    }

    public List<Class> getClasses() {
        return getAll(Class.class);
    }

    public List<Interface> getInterfaces() {
        return getAll(Interface.class);
    }

    public List<Enum> getEnums() {
        return getAll(Enum.class);
    }
}
