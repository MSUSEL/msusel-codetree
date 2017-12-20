package codetree.utils

import com.google.common.collect.Maps

@Singleton
class MetricNameRegistry {

    /**
     * The mapping of names to the actual acronym used by the msusel-metrics
     * module. The index is the alternate name and the value is the known
     * acronym.
     */
    private nameMap = ["LOC": "LOC", "LoC": "LOC", "CountLineCode": "LOC"]

    /**
     * Registers a name acronym and its alternate names. If the acronym is
     * null nothing happens. If their are no alternative names, then simply the
     * acronym is mapped to itself.
     *
     * @param metric
     *            The name acronym
     * @param alts
     *            The alternate names.
     */
    void register(metric, String... alts)
    {
        if (metric == null || metric.isEmpty())
            return;

        nameMap[metric] = metric
        for (String alt : alts)
        {
            nameMap[alt] = metric
        }
    }

    /**
     * Looks up the acronym assigned to the given name.
     *
     * @param name
     *            A name name.
     * @return The acronym associated with the given name, or null if no such
     *         mapping exists or if the provided name is null or empty.
     */
    def lookup(String name)
    {
        if (name == null || name.isEmpty() || !nameMap.containsKey(name))
            return null

        return nameMap[name]
    }
}
