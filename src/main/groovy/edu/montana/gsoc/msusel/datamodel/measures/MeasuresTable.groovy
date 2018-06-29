/**
 * The MIT License (MIT)
 *
 * MSUSEL Metrics
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
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
package edu.montana.gsoc.msusel.datamodel.measures

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import com.google.inject.Inject
import edu.montana.gsoc.msusel.datamodel.DataModelMediator
import edu.montana.gsoc.msusel.datamodel.DefaultDataModelMediator

@Singleton
class MeasuresTable {

    // Table containing the values of metrics stored. Rows are keys for nodes in the codetree, columns are metric handles, and cells are measures
    Table<String, String, Object> measures = HashBasedTable.create()
    @Inject
    DataModelMediator model

    void merge(DataModelMediator tree) {
        if (this.model)
            this.model.merge(tree)
        else
            this.model = tree
    }

    def store(Measure m) {
        measures.put(m.getItemKey(), m.getMetricKey(), m.getValue())
    }

    def retrieve(Measurable node, String metric) {
        measures.get(node.getKey(), metric)
    }

    boolean hasMetric(Measurable c, String metric) {
        measures.get(c.getKey(), metric) != null
    }

    List<Double> getAllClassValues(String metric) {
        List<Double> values = []

        model.getTypes().each { values << (double) retrieve(it, metric) }

        values
    }

    List<Double> getAllFileValues(String metric) {
        List<Double> values = []

        model.getFiles().each { values << (double) retrieve(it, metric) }

        values
    }

    List<Double> getAllMethodValues(String metric) {
        List<Double> values = []

        model.getMethods().each { values << (double) retrieve(it, metric) }

        values
    }

    double getProjectMetric(String metric) {
        if (hasMetric(model.getSystem(), metric))
            (double) retrieve(model.getSystem(), metric)
        else
            0.0d
    }

    void clean() {
        measures.clear()
        model = new DefaultDataModelMediator()
    }
}
