/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
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

    /**
     * Constructs a new BaseCodeTreeBuilder
     * @param identifier
     */
    BaseCodeTreeBuilder(ArtifactIdentifier identifier) {
        this.identifier = identifier
    }

    /**
     * Template method to construct the codetree for the given project key and root project path
     * @param projectKey Project Key
     * @param path root path for the project
     * @return Code Tree constructed for the project
     */
    CodeTree build(String projectKey, String path) {
        ProjectNode root = ProjectNode.builder().key(projectKey).create()
        tree = new CodeTree()
        tree.setProject(root)

        getIdentifier().identify(path)
        final List<Path> files = getIdentifier().getSourceFiles()
        parseStructure(files)
        AssociationExtractor extract = new AssociationExtractor(tree);
        extract.extractAssociations()
        extract.extractDependencies()

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
            gatherTypeMembers()
        }
    }

    /**
     * Extracts the types defined in the file with the provided path
     * @param file String encoding of the path for the file to be analyzed
     */
    abstract void gatherTypes(String file)

    /**
     * Extracts the relationships between types
     */
    abstract void gatherRelationships()

    /**
     * Extracts the members of types
     */
    abstract void gatherTypeMembers()

    /**
     * Executes the parser with the given listener attached
     * @param listener Parse tree listener which extracts information
     */
    abstract void utilizeParser(ParseTreeListener listener)

    /**
     * Finds or creates a NamespaceNode for the given Namespace name.
     * @param namespaceName Name of the namespace to finde or create
     * @return The namespace
     */
    NamespaceNode getOrCreateNamespace(String namespaceName) {
        NamespaceNode current = tree.getProject().getNamespace(namespaceName)
        if (current == null) {

            String[] packageNames = namespaceName.split(/\./)

            for (String seg : packageNames) {
                String pKey = current == null ? "" : current.getKey()
                String key = pKey.isEmpty() ? seg : String.join(".", pKey, seg)

                if (!tree.getProject().hasNamespace(key)) {
                    NamespaceNode parent = current
                    current = NamespaceNode.builder().key(key).parentKey(pKey).create()
                    setParentNamespace(parent, current)
                    tree.getProject().addChild(current)
                } else {
                    NamespaceNode parent = current
                    setParentNamespace(parent, current)
                    current = tree.getProject().getNamespace(key)
                }
            }
        }
        return current
    }

    /**
     * Adds the child namespace to the provided parent namespace
     * @param parent Parent of the child namespace
     * @param child Child namespace
     */
    void setParentNamespace(NamespaceNode parent, NamespaceNode child) {
        if (parent != null && !parent.containsNamespace(child))
            parent.addChild(child)
    }

    /**
     * Finds a type with the given name, first searching the current namespace, then searching fully specified imported types,
     * then checking the types defined in known language specific wildcard imports, then checking automatically included language
     * types, and finally checking unknown wildcard imports. If the type is not defined within the context of the system under analysis
     * it will be constructed as an UnknownTypeNode.
     *
     * @param name The name of the type
     * @return The TypeNode corresponding to the provided type name.
     */
    TypeNode findType(String name) {
        List<String> general = []
        List<String> specific = []

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

    /**
     * A heuristic to determine if the provided type name is fully defined. That is whether the type name has more than one "."
     * @param name Name to evaluate
     * @return true if the name has more than one ".", false otherwise.
     */
    boolean notFullySpecified(String name) {
        return name.count(".") > 1
    }

    /**
     * Retrieves the full name of the the type with the short name provided. This name is constructed by appending the current package name
     * to the provided name, if it is not already present.
     * @param name The name to evaluate.
     * @return The fully qualified name of the type
     */
    String getFullName(String name) {
        if (!types.empty()) {
            "${typeKey()}.${name}"
        } else {
            namespace == null ? name : "${namespace.getKey()}.${name}"
        }
    }

    /**
     * Method to reverse a stack
     * @param stack The stack to reverse
     * @return The reversed stack
     */
    protected static <T> Stack<T> reverseStack(Stack<T> stack) {
        Stack<T> rev = new Stack<>()
        while (!stack.empty())
            rev.push(stack.pop())

        rev
    }

    /**
     *
     * @param name
     * @return
     */
    TypeNode findTypeNode(String name) {
        final String fullName = getFullName(name)
        TypeNode type = tree.getUtils().findType(fullName)
        types.push(type)
        type
    }

    void clearFlags() {
        flags.clear()
    }

    void setFlag(int flag) {
        flags.set(flag)
    }

    void createInitializer(String initKey, boolean instance = true) {
        String key = "${typeKey()}#${initKey}"
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
        methods.peek().setKey(generateMemberKey(name))
    }

    String generateMemberKey(String name) {
        typeKey() + "#" + name
    }

    String typeKey() {
        types.peek().getKey()
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
        params = reverseStack(params)
        Lists.newArrayList(params).each { methods.peek().addParameter(it) }
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
        flags.clear(FORMAL_PARAMETERS)
    }

    protected void setCurrentNodeTypeRefs(AbstractTypeRef ref) {
        if (flags.get(FORMAL_PARAMETERS)) {
            params.peek().setType(ref)
        } else if (flags.get(TYPE_PARAMETER) || flags.get(TYPE_DECL)) {
            typeParams.peek().addBound(ref)
        } else if (flags.get(METHOD_RESULT)) {
            methods.peek().setType(ref)
        } else if (flags.get(EXCEPTION_LIST)) {
            exceptions.push(ref)
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
        generateTypeVarTypeRef(type)
    }

    void finalizeTypeParameter() {
        flags.clear(TYPE_PARAMETER)
    }

    void generateTypeVarTypeRef(String type) {
        TypeVarTypeRef param = TypeVarTypeRef.builder().typeVar(type).create()
        typeParams.push(param)
    }

    void startMethodExceptionList() {
        flags.set(EXCEPTION_LIST)
        exceptions = new Stack<>()
    }

    void finalizeMethodExceptionList() {
        flags.clear(EXCEPTION_LIST)
        exceptions = reverseStack(exceptions)
        List<AbstractTypeRef> refs = Lists.newArrayList(exceptions)
        refs.each { methods.peek().addException(it) }
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
        handleModifiers(modifier, false, false, true)
    }

    void finalizeMethodModifier() {
        flags.clear(METHOD_MODIFIER)
    }

    void startVariableModifier(String modifier) {
        setFlag(VARIABLE_MODIFIER)
        handleModifiers(modifier, false, false, true)
    }

    void finalizeVariableModifier() {
        flags.clear(VARIABLE_MODIFIER)
    }

    void createInnerTypeRef(String type) {
        TypeNode node = findType(type)
        AbstractTypeRef ref = TypeRef.builder().type(node.getKey()).typeName(node.name()).create()
        updateCurrentTypeRef(ref)
        currentTypeRef.push(ref)
    }

    void constructOuterTypeRef(String typeId) {
        TypeNode type = this.findType(typeId)
        AbstractTypeRef ref = TypeRef.builder().type(type.getKey()).typeName(type.name()).create()
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
                ((WildCardTypeRef) currentTypeRef.peek()).addBound(ref)
            } else if (currentTypeRef.peek() instanceof ArrayTypeRef) {
                ((ArrayTypeRef) currentTypeRef.peek()).setRef(ref)
            } else if (currentTypeRef.peek() instanceof TypeVarTypeRef) {
                ((TypeVarTypeRef) currentTypeRef.peek()).addBound(ref)
            }
        }
        else setCurrentNodeTypeRefs(ref)
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
            generateTypeVarTypeRef(type)
        } else {
            updateCurrentTypeRef(TypeVarTypeRef.builder().typeVar(type).create())
        }
    }

    void createTypeVariable(String typeVar) {
        setCurrentNodeTypeRefs(TypeVarTypeRef.builder().typeVar(generateMemberKey(typeVar)).create())
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
        flags.clear(METHOD_RESULT)
    }

    void setMethodIdentifier(String methodId, String dimensions) {
        methods.peek().setKey(generateMemberKey(methodId))
    }

    def findMethodFromSignature(String signature) {
        MethodNode mnode = types.peek().findMethodBySignature(signature)
        if (mnode != null)
            methods.push(mnode)
        else
            log.warn("Could not find method with sig: ${signature}")

        mnode
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

    void handleModifiers(String mod, boolean type, boolean field, boolean method) {
        Accessibility access = handleAccessibility(mod)
        Modifiers modifier = handleNamedModifiers(mod)

        if (type) {
            if (modifier != null)
                types.peek().addModifier(modifier)
            else if (access != null)
                types.peek().setAccessibility(access)
        }
        else if (field) {
            for (FieldNode node : currentNodes) {
                if (modifier != null)
                    node.addModifier(modifier)
                else if (access != null)
                    node.setAccessibility(access)
            }
        }
        else if (method) {
            if (flags.get(CONSTRUCTOR_MODIFIER) || flags.get(METHOD_MODIFIER)) {
                if (access != null)
                    methods.peek().setAccessibility(access)
                else if (modifier != null)
                    methods.peek().addModifier(modifier)
            } else if (flags.get(VARIABLE_MODIFIER)) {
                if (access != null)
                    params.peek().setAccessibility(access)
                else if (modifier != null)
                    params.peek().addModifier(modifier)
            }
        }
    }

    def handleNamedModifiers(String mod) {
        try {
            return Modifiers.valueForJava(mod)
        } catch (IllegalArgumentException e) {

        }

        null
    }

    def handleAccessibility(String mod) {
        try {
            return Accessibility.valueOf(mod.toUpperCase())
        } catch (IllegalArgumentException e) {

        }

        null
    }

    void createFloatingPointTypeRef(String type) {
        createNumericalPrimitiveTypeRef(type)
    }

    void createIntegralTypeRef(String type) {
        createNumericalPrimitiveTypeRef(type)
    }

    void createNumericalPrimitiveTypeRef(String type) {
        AbstractTypeRef ref = PrimitiveTypeRef.getInstance(type)
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
        FieldNode f = FieldNode.builder().key(generateMemberKey(name)).start(start).end(end).create()
        currentNodes.add(f)
    }

    void finalizeFieldNodes() {
        for (FieldNode node : currentNodes) {
            types.peek().addChild(node)
        }

        currentNodes.clear()
        currentTypeRef.clear()
        clearFlags()
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
        if (types.size() > 1) {
            tree.addContainment(type, types.get(types.indexOf(type) - 1))
        }
    }

    void addUseDependency(String name) {
        TypeNode type = findType(name)
        tree.addUse(types.peek(), type)
    }

    def findStaticInitializer(int num) {
        InitializerNode init = types.peek().getStaticInitializer(num)
        if (init != null) methods.push(init)
        init
    }

    def findInstanceInitializer(int num) {
        InitializerNode init = types.peek().getInstanceInitializer(num)
        if (init != null) methods.push(init)
        init
    }
}
