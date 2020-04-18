/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package edu.isu.isuese.datamodel;

import com.google.common.collect.Lists;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Measure extends Model {

    public String getMeasureKey() { return getString("measureKey"); }

    public String getMetricKey() { return getString("metricKey"); }

    public double getValue() { return getDouble("value"); }

    public void setValue(double value) { set("value", value); save(); }

    public void setReference(Reference ref) {
        List<Reference> refs = Lists.newArrayList(getReferences());
        for (Reference r : refs) {
            removeReference(r);
        }
        if (ref != null)
            add(ref);
        save();
    }

    public void removeReference(Reference ref) { remove(ref); save(); }

    public List<Reference> getReferences() { return getAll(Reference.class); }

    public System getParentSystems() {
        Project parent = getParentProject();
        if (parent != null)
            return parent.getParentSystem();
        return null;
    }

    public Project getParentProject() {
        return parent(Project.class);
    }

    public MetricRepository getParentMetricRepository() {
        Metric parent = getParentMetric();
        if (parent != null)
            return parent.getParentMetricRepository();
        return null;
    }

    public Metric getParentMetric() {
        return parent(Metric.class);
    }

    public static Measure of(String metricKey) {
        Measure m = Measure.create("metricKey", metricKey);
        m.save();
        return m;
    }

    public Measure on(Measurable m) {
        Reference ref = Reference.createIt("refKey", m.getRefKey());
        add(ref);
        save();
        return this;
    }

    public Measure on(Reference ref) {
        add(ref);
        save();
        return this;
    }

    public Measure withValue(double value) {
        set("value", value);
        save();
        return this;
    }

    public void store() {
        save();
    }

    public static Measure retrieve(Measurable m, String metricKey) {
        return null;
    }

    public Measure copy(String oldPrefix, String newPrefix) {
        return Measure.of(this.getMetricKey())
                .on(this.getReferences().get(0).copy(oldPrefix, newPrefix))
                .withValue(this.getValue());
    }
}
