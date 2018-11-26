package edu.isu.isuese.datamodel.util;

import edu.isu.isuese.datamodel.Metric;
import edu.isu.isuese.datamodel.MetricRepository;
import edu.montana.gsoc.msusel.util.MetricNameRegistry;

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
