package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class MetricRepositorySpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        MetricRepository repo = new MetricRepository();
        a(repo).shouldBe("valid");
//        //a(repo.errors().get("author")).shouldBeEqual("Author must be provided");
        repo.set("repoKey", "key", "name", "repo");
        a(repo).shouldBe("valid");
        repo.save();
        repo = MetricRepository.findById(1);
        a(repo.getId()).shouldNotBeNull();
        a(repo.get("name")).shouldBeEqual("repo");
        a(repo.get("repoKey")).shouldBeEqual("key");
        a(MetricRepository.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddMetric() {
        MetricRepository repo = MetricRepository.createIt("repoKey", "key", "name", "repo");
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");

        repo.add(metric);
        a(repo.getAll(Metric.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveMetric() {
        MetricRepository repo = MetricRepository.createIt("repoKey", "key", "name", "repo");
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");

        repo.add(metric);
        a(repo.getAll(Metric.class).size()).shouldBeEqual(1);
        repo = MetricRepository.findById(1);
        repo.removeMetric(metric);
        a(repo.getAll(Metric.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        MetricRepository repo = MetricRepository.createIt("repoKey", "key", "name", "repo");
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");

        repo.add(metric);

        a(MetricRepository.count()).shouldBeEqual(1);
        a(Metric.count()).shouldBeEqual(1);
        repo.delete(true);
        a(MetricRepository.count()).shouldBeEqual(0);
        a(Metric.count()).shouldBeEqual(0);
    }
}
