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

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class MetricSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        Metric metric = new Metric();
        a(metric).shouldBe("valid");
//        //a(metric.errors().get("author")).shouldBeEqual("Author must be provided");
        metric.set("metricKey", "metric", "name", "metric", "description", "description");
        a(metric).shouldBe("valid");
        metric.save();
        metric = (Metric) Metric.findAll().get(0);
        a(metric.getId()).shouldNotBeNull();
        a(metric.get("name")).shouldBeEqual("metric");
        a(metric.get("metricKey")).shouldBeEqual("metric");
        a(metric.get("description")).shouldBeEqual("description");
        a(Metric.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddMeasure() {
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        metric.add(meas);

        a(metric.getMeasures().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveMeasure() {
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        metric.add(meas);

        a(metric.getMeasures().size()).shouldBeEqual(1);
        metric = (Metric) Metric.findAll().get(0);
        metric.removeMeasure(meas);
        a(metric.getMeasures().size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");
        Measure meas = Measure.createIt("measureKey", "meas", "value", 1.0);
        metric.add(meas);

        a(Metric.count()).shouldBeEqual(1);
        a(Measure.count()).shouldBeEqual(1);
        metric.delete(true);
        a(Metric.count()).shouldBeEqual(0);
        a(Measure.count()).shouldBeEqual(0);
    }
}
