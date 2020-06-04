package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("finding_data")
@BelongsTo(parent = Finding.class, foreignKeyName = "finding_id")
public class FindingData extends Model {

    public FindingData() {
    }

    public void with(String name, double value) {
        add(FindingDataPoint.builder().handle(name).value(value).create());
    }

    public Finding getParentFinding() {
        return parent(Finding.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FindingData) {
            FindingData fd = (FindingData) obj;
            if (fd.getId().equals(getId()))
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
