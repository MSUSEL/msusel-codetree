/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
 * Copyright (c) 2015-2017 Montana State University, Gianforte School of Computing,
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
package edu.montana.gsoc.msusel.codetree

import codetree.typeref.*
import com.google.common.collect.Lists
import edu.montana.gsoc.msusel.codetree.node.Accessibility
import edu.montana.gsoc.msusel.codetree.node.Modifiers
import edu.montana.gsoc.msusel.codetree.node.member.ConstructorNode
import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.InitializerNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.member.ParameterNode
import edu.montana.gsoc.msusel.codetree.node.structural.FileNode
import edu.montana.gsoc.msusel.codetree.node.structural.NamespaceNode
import edu.montana.gsoc.msusel.codetree.node.structural.ProjectNode
import edu.montana.gsoc.msusel.codetree.node.type.ClassNode
import edu.montana.gsoc.msusel.codetree.node.type.EnumNode
import edu.montana.gsoc.msusel.codetree.node.type.InterfaceNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.node.type.UnknownTypeNode
import edu.montana.gsoc.msusel.codetree.typeref.ArrayTypeRef
import edu.montana.gsoc.msusel.codetree.typeref.PrimitiveTypeRef
import edu.montana.gsoc.msusel.codetree.typeref.TypeRef
import edu.montana.gsoc.msusel.codetree.typeref.TypeVarTypeRef
import edu.montana.gsoc.msusel.codetree.typeref.WildCardTypeRef
import groovy.util.logging.Slf4j
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.tree.ParseTreeListener

import java.nio.file.Path

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Slf4j
abstract class BaseCodeTreeBuilder {

    ArtifactIdentifier identifier
    NamespaceNode namespace
    protected CodeTree tree
    FileNode file

    Stack<TypeNode> types = new Stack<>()
    Stack<AbstractTypeRef> currentTypeRef = new Stack<>()
    Stack<MethodNode> methods = new Stack<>()
    Stack<ParameterNode> params = new Stack<>()
    Stack<AbstractTypeRef> exceptions = new Stack<>()
    Stack<TypeVarTypeRef> typeParams = new Stack<>()
    private List<FieldNode> currentNodes = []

    BitSet flags = new BitSet(9)

    public static final int FORMAL_PARAMETERS = 0
    public static final int VARIABLE_MODIFIER = 1
    public static final int TYPE_PARAMETER = 2
    public static final int CONSTRUCTOR_MODIFIER = 3
    public static final int METHOD_RESULT = 4
    public static final int EXCEPTION_LIST = 5
    public static final int METHOD_MODIFIER = 6
    public static final int TYPE_DECL = 7
    public static final int FIELD_DECL = 8

    BaseCodeTreeBuilder(ArtifactIdentifier identifier) {
        this.identifier = identifier
    }

    CodeTree build(String projectKey, String path) {
        ProjectNode root = ProjectNode.builder().key(projectKey).create()
        tree = new CodeTree()
        tree.setProject(root)

        getIdentifier().identify(path)
        final List<Path> files = getIdentifier().getSourceFiles()
        parseStructure(files)

        tree
    }

    /**
     * Parses the code structure from the provided set of input files, adding
     * this structure to the root project provided.
     *
     * @param files List of input files to be parsed
     */
    void parseStructure(final List<Path> files) {
        files.each { file ->
            try {
                gatherTypes(file.toAbsolutePath().toString())
            } catch (final RecognitionException e) {
                log.warn(e.getMessage(), e)
            }
        }

        files.each { file ->
            setFile(tree.getUtils().getFile(file.toAbsolutePath().toString()))
            gatherRelationships()
            addComponentsToTypes()
        }
    }

    abstract void gatherTypes(String file)

    abstract void gatherRelationships()

    abstract void addComponentsToTypes()

    abstract void utilizeParser(ParseTreeListener listener)

    NamespaceNode getOrCreateNamespace(String packageName) {
        NamespaceNode current = tree.getProject().getNamespace(packageName)
        if (current == null) {

            String[] packageNames = packageName.split(/\./)

            for (String seg : packageNames) {
                String pKey = current == null ? "" : current.getKey()
                String key = pKey.isEmpty() ? seg : String.join(".", pKey, seg)

                if (!tree.getProject().hasNamespace(key)) {
                    NamespaceNode parent = current
                    current = NamespaceNode.builder().key(key).parentKey(pKey).create()
                    if (parent != null && !parent.containsNamespace(current))
                        parent.addChild(current)
                    tree.getProject().addChild(current)
                } else {
                    NamespaceNode parent = current
                    if (parent != null && !parent.containsNamespace(current))
                        parent.addChild(current)
                    current = tree.getProject().getNamespace(key)
                }
            }
        }
        return current
    }

    TypeNode findType(String name) {
        List<String> general = Lists.newArrayList()
        List<String> specific = Lists.newArrayList()

        TypeNode candidate

        if (notFullySpecified(name)) {
            for (String s : file.getImports()) {
                if (s.endsWith("*"))
                    general.add(s)
                else
                    specific.add(s)
            }

            // Check same package
            if (namespace != null)
                candidate = tree.getUtils().findType(namespace.getKey() + "." + name)
            else
                candidate = tree.getUtils().findType(name)

            if (candidate == null) {
                // Check specific imports
                for (String spec : specific) {
                    if (spec.endsWith(name)) {
                        candidate = tree.getUtils().findType("spec")
                    }

                    if (candidate != null)
                        break
                }
            }

            // Check general imports
            if (candidate == null) {
                for (String gen : general) {
                    String imp = gen.replace("*", name)
                    candidate = tree.getUtils().findType(imp)
                    if (candidate != null)
                        break
                }
            }

            // else java.lang
            if (candidate == null) {
                candidate = tree.getUtils().findType("java.lang." + name)
            }
        } else {
            candidate = tree.getUtils().findType(name)
        }

        // In the event that no valid candidate was found, then it is an unknown type
        if (candidate == null) {
            if (!specific.isEmpty()) {
                for (String spec : specific) {
                    if (spec.endsWith(name)) {
                        candidate = UnknownTypeNode.builder().key(spec).create()
                        break
                    }
                }
            }

            if (candidate == null && !general.isEmpty()) {
                candidate = UnknownTypeNode.builder().key(general.get(0).substring(0, general.get(0).lastIndexOf(".")) + name).create()
            }

            if (candidate == null)
                candidate = UnknownTypeNode.builder().key("java.lang." + name).create()

            tree.addUnknownType(candidate)
        }

        candidate
    }

    boolean notFullySpecified(String name) {
        return !name.contains(".")
    }

    String getFullName(String name) {
        if (!types.empty()) {
            return types.peek().getKey() + "." + name
        } else {
            return namespace == null ? name : namespace.getKey() + "." + name
        }
    }

    protected <T> Stack<T> reverseStack(Stack<T> stack) {
        Stack<T> rev = new Stack<>()
        while (!stack.empty())
            rev.push(stack.pop())

        return rev
    }

    TypeNode findTypeNode(String name) {
        final String fullName = getFullName(name)
        TypeNode type = tree.getUtils().findType(fullName)
        types.push(type)
        type
    }

    void handleModifiers(String modifier) {
        Modifiers mod = null
        Accessibility access = null
        try {
            mod = Modifiers.valueForJava(modifier)
        } catch (IllegalArgumentException x) {

        }
        try {
            access = Accessibility.valueOf(modifier.toUpperCase())
        } catch (IllegalArgumentException e) {

        }

        if (flags.get(CONSTRUCTOR_MODIFIER) || flags.get(METHOD_MODIFIER)) {
            if (access != null)
                methods.peek().setAccessibility(access)
            else if (mod != null)
                methods.peek().addModifier(mod)
        } else if (flags.get(VARIABLE_MODIFIER)) {
            if (access != null)
                params.peek().setAccessibility(access)
            else if (mod != null)
                params.peek().addModifier(mod)
        }
    }

    void clearFlags() {
        flags.clear()
    }

    void setFlag(int flag) {
        flags.set(flag)
    }

    void createInitializer(String initKey, boolean instance = true) {
        String key = "${types.peek().getKey()}#${initKey}"
        methods.push(InitializerNode.builder().key(key).instance(instance).create())

        types.peek().addChild(methods.peek())
    }

    void createMethod(constructor = false) {
        if (constructor) {
            methods.push(ConstructorNode.builder().create())
        } else {
            methods.push(MethodNode.builder().create())
        }
    }

    void handleMethodDeclarator(String name) {
        name = types.peek().getKey() + "#" + name
        methods.peek().setKey(name)
    }

    void finalizeMethod(interfaceMethod = false) {
        clearFlags()

        if (interfaceMethod) {
            methods.peek().setAccessibility(Accessibility.PUBLIC)
            methods.peek().addModifier(Modifiers.ABSTRACT)
        }

        setMethodParams()
        types.peek().addChild(methods.pop())
    }

    void setMethodParams() {
        methods.peek().setParams(Lists.newArrayList(reverseStack(params)))
        params = new Stack<>()
    }

    void createFormalParameter(String identifier = "", receiver = false) {
        flags.set(FORMAL_PARAMETERS)

        if (receiver) {
            params.push(ParameterNode.builder().key(identifier).create())
        } else {
            params.push(ParameterNode.builder().create())
        }
    }

    void finalizeFormalParameter() {
        clearFlags()
    }

    protected void setCurrentNodeTypeRefs(AbstractTypeRef ref) {
        if (flags.get(FORMAL_PARAMETERS)) {
            params.peek().setType(ref)
        } else if (flags.get(TYPE_PARAMETER)) {
            typeParams.peek().addBound(ref)
        } else if (flags.get(METHOD_RESULT)) {
            methods.peek().setType(ref)
        } else if (flags.get(EXCEPTION_LIST)) {
            exceptions.push(ref)
        } else if (flags.get(TYPE_DECL)) {
            typeParams.peek().addBound(ref)
        } else if (flags.get(FIELD_DECL)) {
            if (currentTypeRef.empty())
                currentTypeRef.push(ref)
            else if (currentTypeRef.peek() instanceof ArrayTypeRef)
                ((ArrayTypeRef) currentTypeRef.peek()).setRef(ref)

            for (FieldNode node : currentNodes) {
                node.setType(currentTypeRef.peek())
            }
        }

    }

    void handleVarDeclId(String identifier) {
        if (flags.get(FORMAL_PARAMETERS)) {
            params.peek().setKey(identifier)
        }
    }

    void createTypeParameter(String type) {
        flags.set(TYPE_PARAMETER)
        TypeVarTypeRef param = TypeVarTypeRef.builder().typeVar(type).create()
        typeParams.push(param)
    }

    void startMethodExceptionList() {
        flags.set(EXCEPTION_LIST)
    }

    void finalizeMethodExceptionList() {
        clearFlags()
        List<AbstractTypeRef> refs = Lists.newArrayList(reverseStack(exceptions))
        refs.each { methods.peek().addException(it) }
        exceptions = new Stack<>()
    }

    void startTypeParamList() {
        typeParams = new Stack<>()
    }

    void finalizeTypeParamList() {
        if (!methods.isEmpty())
            methods.peek().setTypeParams(Lists.newArrayList(reverseStack(typeParams)))
        else if (flags.get(TYPE_DECL)) {
            types.peek().setTemplateParams(Lists.newArrayList(reverseStack(typeParams)))
        }
    }

    void startMethodModifier(String modifier) {
        setFlag(METHOD_MODIFIER)
        handleModifiers(modifier)
    }

    void finalizeMethodModifier() {
        clearFlags()
    }

    void startVariableModifier(String modifier) {
        setFlag(VARIABLE_MODIFIER)
        handleModifiers(modifier)
    }

    void createTypeRef(String type) {
        TypeNode node = findType(type)
        AbstractTypeRef ref = TypeRef.builder().type(node).create()
        updateCurrentTypeRef(ref)
        currentTypeRef.push(ref)
    }

    void constructTypeRef(String typeId) {
        TypeNode type = this.findType(typeId)
        AbstractTypeRef ref = TypeRef.builder().type(type).create()
        setCurrentNodeTypeRefs(ref)
        currentTypeRef.push(ref)
    }

    void finalizeTypeRef() {
        currentTypeRef.pop()
    }

    void createArrayTypeRef(String dims, update = false) {
        AbstractTypeRef ref = ArrayTypeRef.builder().dimensions(dims).create()
        if (update) {
            updateCurrentTypeRef(ref)

        }
        currentTypeRef.push(ref)
    }

    void createPrimitiveTypeRef(String type) {
        setCurrentNodeTypeRefs(PrimitiveTypeRef.getInstance(type))
    }

    void updateCurrentTypeRef(AbstractTypeRef ref) {
        if (!currentTypeRef.empty()) {
            if (currentTypeRef.peek() instanceof TypeRef) {
                ((TypeRef) currentTypeRef.peek()).addTypeArg(ref)
            } else if (currentTypeRef.peek() instanceof WildCardTypeRef) {
                System.out.println("Adding Bound: " + ref.name())
                ((WildCardTypeRef) currentTypeRef.peek()).addBound(ref)
            } else if (currentTypeRef.peek() instanceof ArrayTypeRef) {
                ((ArrayTypeRef) currentTypeRef.peek()).setRef(ref)
            } else if (currentTypeRef.peek() instanceof TypeVarTypeRef) {
                ((TypeVarTypeRef) currentTypeRef.peek()).addBound(ref)
            }
        }
    }

    void createWildcardTypeRef() {
        AbstractTypeRef ref = WildCardTypeRef.builder().create()
        updateCurrentTypeRef(ref)
        currentTypeRef.push(ref)
    }

    void constructNamespaceNode(String ns) {
        namespace = getOrCreateNamespace(ns)
        if (!namespace.hasFile(file.getKey())) {
            namespace.addChild(file)
        }
    }

    void createTypeVarTypeRef(String type) {
        if (flags.get(TYPE_DECL)) {
            TypeVarTypeRef param = TypeVarTypeRef.builder().typeVar(type).create()
            typeParams.push(param)
        } else
            updateCurrentTypeRef(TypeVarTypeRef.builder().typeVar(type).create())
    }

    void createTypeVariable(String typeVar) {
        setCurrentNodeTypeRefs(TypeVarTypeRef.builder().typeVar(types.peek().getKey() + "#" + typeVar).create())
    }

    void dropTypeNode() {
        clearFlags()
        types.pop()
    }

    void startMethodReturnType(voidType) {
        flags.set(METHOD_RESULT)

        if (voidType) {
            setCurrentNodeTypeRefs(PrimitiveTypeRef.getInstance("void"))
        }
    }

    void finalizeMethodReturnType() {
        clearFlags()
    }

    void setMethodIdentifier(String methodId, String dimensions) {
        methodId = types.peek().getKey() + "#" + methodId
        methods.peek().setKey(methodId)
    }

    void findMethodFromSignature(String signature) {
        MethodNode mnode = types.peek().findMethodBySignature(signature)
        if (mnode != null)
            methods.push(mnode)
        else
            log.warn("Could not find method with sig: ${signature}")
    }

    void createClassNode(String name, int start, int end) {
        createTypeNode(name, start, end, ClassNode.builder())
    }

    void createEnumNode(String name, int start, int end) {
        createTypeNode(name, start, end, EnumNode.builder())
    }

    void createInterfaceNode(String name, int start, int end) {
        createTypeNode(name, start, end, InterfaceNode.builder())
    }

    private createTypeNode(String name, int start, int end, builder) {
        setFlag(TYPE_DECL)

        final String fullName = getFullName(name)
        final TypeNode node = builder.key(fullName).start(start).end(end).create()
        types.push(node)
        file.addChild(node)
        if (namespace != null)
            namespace.addChild(node)
    }

    void handleTypeModifier(String mod) {
        Accessibility access = null
        try {
            access = Accessibility.valueOf(mod.toUpperCase())
        } catch (IllegalArgumentException e) {

        }

        Modifiers modifier = null
        try {
            modifier = Modifiers.valueForJava(mod)
        } catch (IllegalArgumentException e) {

        }

        if (modifier != null)
            types.peek().addModifier(modifier)
        else if (access != null)
            types.peek().setAccessibility(access)
    }

    void handleFieldModifier(String mod) {
        Accessibility access = null
        try {
            access = Accessibility.valueOf(mod.toUpperCase());
        } catch (IllegalArgumentException e) {

        }

        Modifiers modifier = null
        try {
            modifier = Modifiers.valueForJava(mod)
        } catch (IllegalArgumentException e) {

        }

        for (FieldNode node : currentNodes) {
            if (modifier != null)
                node.addModifier(modifier)
            else if (access != null)
                node.setAccessibility(access)
        }
    }

    void createFloatingPointTypeRef(String type) {
        AbstractTypeRef ref = PrimitiveTypeRef.getInstance(type);
        if (!currentTypeRef.empty() && currentTypeRef.peek() instanceof ArrayTypeRef)
            ((ArrayTypeRef) currentTypeRef.peek()).setRef(ref)
        else
            currentTypeRef.push(ref)

        for (FieldNode node : currentNodes) {
            node.setType(currentTypeRef.peek())
        }
    }

    void createIntegralTypeRef(String type) {
        AbstractTypeRef ref = PrimitiveTypeRef.getInstance(type);
        if (!currentTypeRef.empty() && currentTypeRef.peek() instanceof ArrayTypeRef)
            ((ArrayTypeRef) currentTypeRef.peek()).setRef(ref)
        else
            currentTypeRef.push(ref)

        for (FieldNode node : currentNodes) {
            node.setType(currentTypeRef.peek())
        }
    }

    void createFieldNode(String name, int start, int end) {
        flags.set(FIELD_DECL)
        FieldNode f = FieldNode.builder().key(types.peek().getKey() + "#" + name).start(start).end(end).create()
        currentNodes.add(f)
    }

    void finalizeFieldNodes() {
        for (FieldNode node : currentNodes) {
            types.peek().addChild(node)
        }

        currentNodes.clear()
        currentTypeRef.clear()
    }

    void createRealization(TypeNode type, String implementsType) {
        TypeNode other = findType(implementsType)
        //if (!(other instanceof UnknownTypeNode)) {
        if (other != null)
            tree.addRealizes(type, other)
        //}
    }

    void createGeneralization(TypeNode type, String genType) {
        TypeNode other = findType(genType)
        //if (!(other instanceof UnknownTypeNode)) {
        if (other != null)
            tree.addGeneralizes(type, other)
        //}
    }

    void createContainment(TypeNode type) {
        if (!types.empty()) {
            tree.addContainment(type, types.peek())
        }
    }
}
