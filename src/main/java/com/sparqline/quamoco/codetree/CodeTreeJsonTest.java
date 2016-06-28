/**
 * 
 */
package com.sparqline.quamoco.codetree;

/**
 * @author fate
 *
 */
public class CodeTreeJsonTest {

    public static void main(String args[]) {
        ProjectNode pn = new ProjectNode("project");
        FileNode fn = new FileNode("/home/git/test");

        fn.addMetric("TEST", 1.0);
        fn.addMetric("TEST2", 2.0);

        TypeNode cn = fn.addType("Class");
        cn.updateLocation(1, 100);

        cn.addMetric("TEST1", 1.0);

        MethodNode mn = cn.addMethod("Method");

        mn.updateLocation(50, 100);
        mn.addMetric("Test1", 1.0);

        FieldNode fdn = cn.addField("TestField");
        fdn.updateLocation(25, 25);

        fdn.addMetric("Test1", 1.0);

        System.out.println(fn.toJSON());

        FileNode f = (FileNode) FileNode.createFromJson(fn.toJSON());

        CodeTree tree = new CodeTree();
        tree.setProject(pn);
        pn.addFile(fn);

        System.out.println("\n" + tree.toJSON());
        
        CodeTree ct2 = CodeTree.createFromJson(tree.toJSON());
        
        ProjectNode pn2 = ProjectNode.createFromJson(pn.toJSON());
        
        tree = new CodeTree();
        tree.setProject("Bob");
        
        System.out.println("\n" + tree.toJSON());
        
        ct2 = CodeTree.createFromJson(tree.toJSON());
    }
}
