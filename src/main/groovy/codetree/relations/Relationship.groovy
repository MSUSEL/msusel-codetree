package codetree.relations

import codetree.INode
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * A basic relationship class used to connect entities within a code tree
 * outside the confines of the containment relationships inherent to the tree
 * structure.
 *
 * @author Isaac Griffith`
 * @version 1.1.0
 */
@EqualsAndHashCode
@ToString
public class Relationship {

    /**
     * The type of this relationship
     */
    def type;

    /**
     * The node acting as the start point of this relationship
     */
    def source;
    /**
     * The node acting as the end point of this relationship
     */
    def dest;
}