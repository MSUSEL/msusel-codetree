/**
 * 
 */
package edu.montana.gsoc.msusel;

import edu.montana.gsoc.msusel.CodeTree;
import edu.montana.gsoc.msusel.node.FieldNode;
import edu.montana.gsoc.msusel.node.FileNode;
import edu.montana.gsoc.msusel.node.MethodNode;
import edu.montana.gsoc.msusel.node.ProjectNode;
import edu.montana.gsoc.msusel.node.TypeNode;

/**
 * @author fate
 */
public class CodeTreeJsonTest {

    public static void main(String args[])
    {
        ProjectNode pn = ProjectNode.builder("project").metric("TEST", 4.0).metric("TEST2", 10.0).create();

        FileNode fn = FileNode.builder("/home/git/test")
                .metric("TEST", 1.0)
                .metric("TEST2", 2.0)
                .type(
                        TypeNode.builder("Class", "Class")
                                .range(1, 100)
                                .metric("TEST1", 1.0)
                                .method(
                                        MethodNode.builder("Method", "Method")
                                                .range(50, 100)
                                                .metric("Test1", 1.0)
                                                .create())
                                .field(
                                        FieldNode.builder("TestField", "TestField")
                                                .range(25, 25)
                                                .metric("Test1", 1.0)
                                                .create())
                                .create())
                .create();

        FileNode f = (FileNode) FileNode.createFromJson(fn.toJSON());

        CodeTree tree = new CodeTree();
        tree.setProject(pn);
        pn.addFile(fn);

        System.out.println("\n" + tree.toJSON());

        CodeTree ct2 = CodeTree.createFromJson(tree.toJSON());

        // ProjectNode pn2 = ProjectNode.createFromJson(pn.toJSON());
        //
        // tree = new CodeTree();
        // tree.setProject("Bob");
        //
        // System.out.println("\n" + tree.toJSON());
        //
        // ct2 = CodeTree.createFromJson(tree.toJSON());

        System.out.println("\n\n" + ct2.toJSON());
    }
}
