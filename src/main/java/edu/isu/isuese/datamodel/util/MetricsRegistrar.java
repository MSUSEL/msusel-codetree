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
package edu.isu.isuese.datamodel.util;

import edu.isu.isuese.datamodel.Metric;
import edu.isu.isuese.datamodel.MetricRepository;
//import edu.montana.gsoc.msusel.util.MetricNameRegistry;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class MetricsRegistrar {

    public void register(String repoKey, String metricKey, String metricName, String description) {
        MetricRepository repo = MetricRepository.findFirst("repoKey = ?", repoKey);
        if (repo == null) {
            repo = MetricRepository.createIt("repoKey", repoKey, "name", repoKey);
        }
        Metric m = Metric.findFirst("name = ? and metrickKey = ? and metric_repository_id = ?", metricName, metricKey, repo.getId());
        if (m == null) {
            Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");
            repo.add(metric);
        }
    }

    public Metric lookup(String name) {
        return Metric.findFirst("name = ?", name);
    }

    /**
     * @return The single instance of this registry.
     */
    public static MetricsRegistrar getInstance()
    {
        return MetricsRegistrar.RegistryHolder.INSTANCE;
    }

    /**
     * A static inner class used to hold the single instance of
     * MetricNameRegistry.
     *
     * @author Isaac Griffith
     * @version 1.3.0
     */
    private static class RegistryHolder {

        /**
         * The singleton instance of MetricNameRegistry
         */
        private static final MetricsRegistrar INSTANCE = new MetricsRegistrar();
    }
}
