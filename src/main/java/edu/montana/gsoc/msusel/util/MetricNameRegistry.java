/**
 * The MIT License (MIT)
 *
 * SparQLine Code Tree
 * Copyright (c) 2015-2017 Isaac Griffith, SparQLine Analytics, LLC
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
package edu.montana.gsoc.msusel.util;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * A registration utility for metric names, given that most metrics have
 * different names depending on the tools producing them. Note that this is
 * implemented as a Singleton.
 * 
 * @author Isaac Griffith
 * @version 1.1.0
 */
public class MetricNameRegistry {

    /**
     * The mapping of names to the actual acronym used by the sparqline-metrics
     * module. The index is the alternate name and the value is the known
     * acronym.
     */
    private Map<String, String> nameMap;

    /**
     * @return The single instance of this registry.
     */
    public static MetricNameRegistry getInstance()
    {
        return RegistryHolder.INSTANCE;
    }

    /**
     * Constructs a new metric registry with the names for LOC already entered.
     */
    private MetricNameRegistry()
    {
        nameMap = Maps.newHashMap();
        nameMap.put("LOC", "LOC");
        nameMap.put("LoC", "LOC");
        nameMap.put("CountLineCode", "LOC");
    }

    /**
     * Registers a metric acronym and its alternate names. If the acronym is
     * null nothing happens. If their are no alternative names, then simply the
     * acronym is mapped to itself.
     * 
     * @param metric
     *            The metric acronym
     * @param alts
     *            The alternate names.
     */
    public void register(String metric, String... alts)
    {
        if (metric == null || metric.isEmpty())
            return;

        nameMap.put(metric, metric);
        for (String alt : alts)
        {
            nameMap.put(alt, metric);
        }
    }

    /**
     * Looks up the acronym assigned to the given name.
     * 
     * @param name
     *            A metric name.
     * @return The acronym associated with the given name, or null if no such
     *         mapping exists or if the provided name is null or empty.
     */
    public String lookup(String name)
    {
        if (name == null || name.isEmpty() || !nameMap.containsKey(name))
            return null;

        return nameMap.get(name);
    }

    /**
     * A static inner class used to hold the single instance of
     * MetricNameRegistry.
     * 
     * @author Isaac Griffith
     * @version 1.1.0
     */
    private static class RegistryHolder {

        /**
         * The singleton instance of MetricNameRegistry
         */
        private static final MetricNameRegistry INSTANCE = new MetricNameRegistry();
    }
}
