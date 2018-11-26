package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MetricRepository extends Model {

    public String getRepoKey() { return getString("repoKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addMetric(Metric metric) { add(metric); save(); }

    public void removeMetric(Metric metric) { remove(metric); save(); }

    public List<Metric> getMetrics() { return getAll(Metric.class); }
}
