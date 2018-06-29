/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
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

import groovy.transform.EqualsAndHashCode
import groovy.transform.builder.Builder

import javax.persistence.*
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Entity
@Builder(buildMethodName = "create", excludes = ["id"])
@EqualsAndHashCode(excludes = ["id"])
class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id
    String metricKey
    String name
    String description
    @OneToMany(mappedBy = "metric")
    List<Measure> measures = []
    @ManyToOne(fetch = FetchType.LAZY)
    MetricRepository repository

    Metric leftShift(Measure measure) {
        addMeasure(measure)
        this
    }

    Metric plus(Measure measure) {
        addMeasure(measure)
        this
    }

    Metric minus(Measure measure) {
        removeMeasure(measure)
    }

    void addMeasure(Measure measure) {
        if (measure && !measures.contains(measure)) {
            measures << measure
            measure.metric = this
        }
    }

    void removeMeasure(Measure measure) {
        if (measure && measures.contains(measure)) {
            measures -= measure
            measure.metric = null
        }
    }
}
