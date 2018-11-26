package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Class.class, Enum.class, Interface.class})
public class Field extends TypedMember {
}
