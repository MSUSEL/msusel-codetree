package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Module extends Model implements Measureable {

    public String getModuleKey() { return getString("moduleKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addNamespace(Namespace ns) { add(ns); save(); }

    public void removeNamespace(Namespace ns) { remove(ns); save(); }

    public List<Namespace> getNamespaces() { return getAll(Namespace.class); }
}
