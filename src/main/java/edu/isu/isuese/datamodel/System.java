package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class System extends Model implements Measureable {

    public static void main(String[] args) {
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:data/dev.db", "dev1", "");
        System sys = System.createIt("version",  "1.0", "language", "java", "sysKey", "1.0");
        sys.setId(2);

        System sys2 = System.findById(2);
        java.lang.System.out.println(sys2);
        Base.close();
    }

    public void addProject(Project p) { add(p); save(); }

    public void removeProject(Project p) { remove(p); save(); }

    public List<Project> getProjects() {
        return getAll(Project.class);
    }

    public void addPatternChain(PatternChain p) { add(p); save(); }

    public void removePatternChain(PatternChain p) { remove(p); save(); }

    public List<PatternChain> getPatternChains() { return getAll(PatternChain.class); }
}
