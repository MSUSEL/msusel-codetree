package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Metric extends Model {

    public String getMetricKey() { return getString("metricKey"); }

    public String getDescription() { return getString("description"); }

    public void getDescription(String desc) { set("description", desc); save(); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addMeasure(Measure measure) { add(measure); save(); }

    public void removeMeasure(Measure measure) { remove(measure); save(); }

    public List<Measure> getMeasures() { return getAll(Measure.class); }
}
