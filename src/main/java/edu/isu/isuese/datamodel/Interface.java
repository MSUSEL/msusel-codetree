package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Class.class, Interface.class, Enum.class})
@BelongsToParents({
        @BelongsTo(foreignKeyName="file_id",parent=File.class),
})
public class Interface extends Classifier {
}
