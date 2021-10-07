/*
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
import com.google.common.collect.Sets;
import edu.isu.isuese.datamodel.util.DbUtils;
import lombok.Builder;
import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.Set;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MetricRepository extends Model {

    public MetricRepository() {}

    @Builder(buildMethodName = "create")
    public MetricRepository(String key, String name) {
        if (key != null && !key.isEmpty()) set("repoKey", key);
        if (name != null && !name.isEmpty()) setName(name);

        save();
    }

    public String getRepoKey() { return getString("repoKey"); }

    public String getName() { return getString("name"); }

    public void setName(String name) { set("name", name); save(); }

    public void addMetric(Metric metric) { add(metric); save(); }

    public void removeMetric(Metric metric) { remove(metric); save(); }

    public List<Metric> getMetrics() { return getAll(Metric.class); }

    public List<Measure> getMeasures() {
        Set<Measure> measures = Sets.newHashSet();
        getMetrics().forEach(metric -> {
            measures.addAll(metric.getMeasures());
        });
        return Lists.newArrayList(measures);
    }
}
