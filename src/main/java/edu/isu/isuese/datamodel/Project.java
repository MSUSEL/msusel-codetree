package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Project extends Model implements Measureable {

    public static void main(String[] args) {
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:data/dev.db", "dev1", "");
        //Project sys = Project.createIt("version",  "1.0", "name", "java", "projKey", "3.0");

        Project sys2 = Project.findById(1);
        java.lang.System.out.println(sys2);
        Base.close();
    }

    public String getProjectKey() { return getString("projKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public String getVersion() { return getString("version"); }

    public void setVersion(String version) { set("version", version); save(); }

    public void addLanguage(Language lang) { add(lang); save(); }

    public void removeLanguage(Language lang) { remove(lang); save(); }

    public List<Language> getLanguages() { return getAll(Language.class); }

    public void addMeasure(Measure meas) { add(meas); save(); }

    public void removeMeasure(Measure meas) { remove(meas); save(); }

    public List<Measure> getMeasures() { return getAll(Measure.class); }

    public void addFinding(Finding find) { add(find); save(); }

    public void removeFinding(Finding find) { remove(find); save(); }

    public List<Finding> getFindings() { return getAll(Finding.class); }

    public void addPatternInstance(PatternInstance inst) { add(inst); save(); }

    public void removePatternInstance(PatternInstance inst) { remove(inst); save(); }

    public List<PatternInstance> getPatternInstances() { return getAll(PatternInstance.class); }

    public void addSCM(SCM scm) { add(scm); save(); }

    public void removeSCM(SCM scm) { remove(scm); save(); }

    public List<SCM> getSCMs() { return getAll(SCM.class); }

    public void addModule(Module mod) { add(mod); save(); }

    public void removeModule(Module mod) { remove(mod); save(); }

    public List<Module> getModules() { return getAll(Module.class); }

    public void addRelation(Relation rel) { add(rel); save(); }

    public void removeRelation(Relation rel) { remove(rel); save(); }

    public List<Relation> getRelations() { return getAll(Relation.class); }
}
