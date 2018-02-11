/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
 * Copyright (c) 2015-2017 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
package edu.montana.gsoc.msusel.codetree.utils

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
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
