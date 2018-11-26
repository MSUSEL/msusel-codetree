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
        metric = Metric.findById(1);
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
        metric = Metric.findById(1);
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
