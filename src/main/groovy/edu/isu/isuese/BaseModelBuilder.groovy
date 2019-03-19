/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
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
package edu.isu.isuese

import com.google.common.collect.Lists
import edu.isu.isuese.datamodel.Accessibility
import edu.isu.isuese.datamodel.Constructor
import edu.isu.isuese.datamodel.Destructor
import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Initializer
import edu.isu.isuese.datamodel.Member
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Modifier
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Parameter
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.RelationType
import edu.isu.isuese.datamodel.System
import edu.isu.isuese.datamodel.Class
import edu.isu.isuese.datamodel.Interface
import edu.isu.isuese.datamodel.Enum
import edu.isu.isuese.datamodel.RefType
import edu.isu.isuese.datamodel.Reference
import edu.isu.isuese.datamodel.Type
import edu.isu.isuese.datamodel.TypeRef
import edu.isu.isuese.datamodel.TypeRefType
import edu.isu.isuese.datamodel.UnknownType
import groovy.util.logging.Slf4j
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.tree.ParseTreeListener

import java.nio.file.Path

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Slf4j
abstract class BaseModelBuilder {

    ArtifactIdentifier identifier
    System system
    Project proj
    Namespace namespace
    File file

    Stack<Type> types = new Stack<>()
    Stack<TypeRef> currentTypeRef = new Stack<>()
    Stack<Member> methods = new Stack<>()
    Stack<Parameter> params = new Stack<>()
    Stack<TypeRef> exceptions = new Stack<>()
    Stack<TypeRef> typeParams = new Stack<>()
    private List<Field> currentNodes = []

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
     * Constructs a new BaseModelBuilder
     * @param identifier
     */
    BaseModelBuilder(ArtifactIdentifier identifier) {
        this.identifier = identifier
    }

    /**
     * Template method to construct the codetree for the given project key and root project path
     * @param projectKey Project Key
     * @param path root path for the project
     * @return Code Tree constructed for the project
     */
    System build(String projectKey, String path) {
        proj = Project.builder().projKey(projectKey).version().create()
        system = System.builder()

        getIdentifier().identify(path)
        final List<Path> files = getIdentifier().getSourceFiles()
        parseStructure(files)
        AssociationExtractor extract = new AssociationExtractor(proj)
        extract.extractAssociations()
        extract.extractDependencies()

        system
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
            setFile(proj.getFile(file.toAbsolutePath().toString()))
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
     * @param listener Parse model listener which extracts information
     */
    abstract void utilizeParser(ParseTreeListener listener)

    /**
     * Finds or creates a Namespace for the given Namespace name.
     * @param namespaceName Name of the namespace to find or create
     * @return The namespace
     */
    Namespace getOrCreateNamespace(String namespaceName) {
        Namespace current = proj.findNamespace(namespaceName)
        if (current == null) {

            String[] packageNames = namespaceName.split(/\./)

            for (String seg : packageNames) {
                String pKey = current == null ? "" : current.getNsKey()
                String key = pKey.isEmpty() ? seg : String.join(".", pKey, seg) // TODO fix this

                if (!proj.hasNamespace(key)) {
                    Namespace parent = current
                    current = Namespace.createIt("nsKey", key, "name", key)
                    parent.addNamespace(current)
                } else {
                    Namespace parent = current
                    setParentNamespace(parent, current)
                    current = proj.findNamespace(key)
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
    void setParentNamespace(Namespace parent, Namespace child) {
        if (parent != null && !parent.containsNamespace(child))
            parent.addNamespace(child)
    }

    /**
     * Finds a type with the given name, first searching the current namespace, then searching fully specified imported types,
     * then checking the types defined in known language specific wildcard imports, then checking automatically included language
     * types, and finally checking unknown wildcard imports. If the type is not defined within the context of the system under analysis
     * it will be constructed as an UnknownType.
     *
     * @param name The name of the type
     * @return The Type corresponding to the provided type name.
     */
    Type findType(String name) {
        List<String> general = []
        List<String> specific = []

        Type candidate

        if (notFullySpecified(name)) {
            for (String s : file.getImports()) {
                if (s.endsWith("*"))
                    general.add(s)
                else
                    specific.add(s)
            }

            // Check same package
            if (namespace != null)
                candidate = proj.findType(namespace.getName() + "." + name)
            else
                candidate = proj.findType(name)

            if (candidate == null) {
                // Check specific imports
                for (String spec : specific) {
                    if (spec.endsWith(name)) {
                        candidate = proj.findType("spec")
                    }

                    if (candidate != null)
                        break
                }
            }

            // Check general imports
            if (candidate == null) {
                for (String gen : general) {
                    String imp = gen.replace("*", name)
                    candidate = proj.findType(imp)
                    if (candidate != null)
                        break
                }
            }

            // else java.lang
            if (candidate == null) {
                candidate = proj.findType("java.lang." + name)
            }
        } else {
            candidate = proj.findType(name)
        }

        // In the event that no valid candidate was found, then it is an unknown type
        if (candidate == null) {
            if (!specific.isEmpty()) {
                for (String spec : specific) {
                    if (spec.endsWith(name)) {
                        candidate = UnknownType.builder().compKey(spec).create()
                        break
                    }
                }
            }

            if (candidate == null && !general.isEmpty()) {
                candidate = UnknownType.builder().compKey(general.get(0).substring(0, general.get(0).lastIndexOf(".")) + name).create()
            }

            if (candidate == null)
                candidate = UnknownType.builder().compKey("java.lang." + name).create()

            if (candidate != null)
                proj.addUnknownType((UnknownType) candidate)
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
            namespace == null ? name : "${namespace.getName()}\$${name}"
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
    Type findTypeNode(String name) {
        final String fullName = getFullName(name)
        Type type = proj.findType(fullName)
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
        methods.push(Initializer.builder().compKey(key).instance(instance).create()) // FIXME need to add more info
        types.peek() << methods.peek()
    }

    void createMethod(constructor = false) {
        if (constructor) {
            methods.push(Constructor.builder().create())
        } else {
            methods.push(Method.builder().create())
        }
    }

    void handleMethodDeclarator(String name) {
        methods.peek().setCompKey(generateMemberKey(name))
    }

    String generateMemberKey(String name) {
        typeKey() + "#" + name
    }

    String typeKey() {
        types.peek().getCompKey()
    }

    void finalizeMethod(interfaceMethod = false) {
        clearFlags()

        if (interfaceMethod) {
            methods.peek().setAccessibility(Accessibility.PUBLIC)
            methods.peek().addModifier(Modifier.Values.ABSTRACT.toString())
        }

        setMethodParams()
        types.peek() << methods.pop()
    }

    void setMethodParams() {
        params = reverseStack(params)
        Lists.newArrayList(params).each { ((Method) methods.peek()).addParameter(it) }
        params = new Stack<>()
    }

    void createFormalParameter(String identifier = "", receiver = false) {
        flags.set(FORMAL_PARAMETERS)
        if (receiver) {
            params.push(Parameter.builder().name(identifier).create())
        } else {
            params.push(Parameter.builder().create())
        }
    }

    void finalizeFormalParameter() {
        flags.clear(FORMAL_PARAMETERS)
    }

    protected void setCurrentNodeTypeRefs(TypeRef ref) {
        if (flags.get(FORMAL_PARAMETERS)) {
            params.peek().setType(ref)
        } else if (flags.get(TYPE_PARAMETER) || flags.get(TYPE_DECL)) {
            typeParams.peek().addBound(ref)
        } else if (flags.get(METHOD_RESULT)) {
            if (methods.peek() instanceof Method)
                ((Method) methods.peek()).setType(ref)
        } else if (flags.get(EXCEPTION_LIST)) {
            exceptions.push(ref)
        } else if (flags.get(FIELD_DECL)) {
            if (currentTypeRef.empty())
                currentTypeRef.push(ref)
            else if (currentTypeRef.peek().ref == null && currentTypeRef.peek().dimensions != null)
                currentTypeRef.peek().setReference(ref.getReferences()?.get(0))
            for (Field node : currentNodes) {
                node.setType(currentTypeRef.peek())
            }
        }

    }

    void handleVarDeclId(String identifier) {
        if (flags.get(FORMAL_PARAMETERS)) {
            params.peek().setName(identifier)
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
        TypeRef param = TypeRef.createTypeVarTypeRef(type)
        typeParams.push(param)
    }

    void startMethodExceptionList() {
        flags.set(EXCEPTION_LIST)
        exceptions = new Stack<>()
    }

    void finalizeMethodExceptionList() {
        flags.clear(EXCEPTION_LIST)
        exceptions = reverseStack(exceptions)
        List<TypeRef> refs = Lists.newArrayList(exceptions)
        refs.each {
            if (methods.peek() instanceof Method)
                ((Method) methods.peek()).addException(it)
            else if (methods.peek() instanceof Constructor)
                ((Constructor) methods.peek()).addException(it)
            else if (methods.peek() instanceof Destructor)
                ((Destructor) methods.peek()).addException(it)
        }
    }

    void startTypeParamList() {
        typeParams = new Stack<>()
    }

    void finalizeTypeParamList() {
        if (!methods.isEmpty()) {
            if (methods.peek() instanceof Method)
                ((Method) methods.peek()).setTypeParams(Lists.newArrayList(reverseStack(typeParams)))
            else if (methods.peek() instanceof Constructor)
                ((Constructor) methods.peek()).setTypeParams(Lists.newArrayList(reverseStack(typeParams)))
            else if (methods.peek() instanceof Destructor)
                ((Destructor) methods.peek()).setTypeParams(Lists.newArrayList(reverseStack(typeParams)))
        }
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
        Type node = findType(type)
        TypeRef ref = TypeRef.builder()
                .ref(Reference.builder()
                    .refKey(node.getCompKey())
                    .refType(RefType.TYPE)
                    .create())
                .typeName(node.getName())
                .create()
        updateCurrentTypeRef(ref)
        currentTypeRef.push(ref)
    }

    void constructOuterTypeRef(String typeId) {
        Type type = this.findType(typeId)
        TypeRef ref = TypeRef.builder()
                .ref(Reference.builder()
                    .refKey(type.getCompKey())
                    .refType(RefType.TYPE)
                    .create())
                .typeName(type.getName())
                .create()
        setCurrentNodeTypeRefs(ref)
        currentTypeRef.push(ref)
    }

    void finalizeTypeRef() {
        currentTypeRef.pop()
    }

    void createArrayTypeRef(String dims, update = false) {
        TypeRef ref = TypeRef.builder().dimensions(dims).create()
        if (update) {
            updateCurrentTypeRef(ref)
        }
        currentTypeRef.push(ref)
    }

    void createPrimitiveTypeRef(String type) {
        setCurrentNodeTypeRefs(TypeRef.createPrimitiveTypeRef(type))
    }

    void updateCurrentTypeRef(TypeRef ref) {
        if (!currentTypeRef.empty()) {
            switch (currentTypeRef.peek().type) {
                case TypeRefType.WildCard || TypeRefType.TypeVar:
                    currentTypeRef.peek().addBound(ref)
                    break
                case TypeRefType.Primitive:
                    break
                default:
                    if (currentTypeRef.peek().reference == null && currentTypeRef.peek().typeName == null) {
                        currentTypeRef.peek().setReference(ref.getReferences().first())
                        currentTypeRef.peek().setTypeName(ref.getTypeName())
                        currentTypeRef.peek().setType(ref.getType())
                    } else {
                        currentTypeRef.peek().addTypeArg(ref)
                    }
            }
        }
        else setCurrentNodeTypeRefs(ref)
    }

    void createWildcardTypeRef() {
        TypeRef ref = TypeRef.createWildCardTypeRef()
        updateCurrentTypeRef(ref)
        currentTypeRef.push(ref)
    }

    void constructNamespaceNode(String ns) {
        namespace = getOrCreateNamespace(ns)
    }

    void createTypeVarTypeRef(String type) {
        if (flags.get(TYPE_DECL)) {
            generateTypeVarTypeRef(type)
        } else {
            updateCurrentTypeRef(TypeRef.createTypeVarTypeRef(type))
        }
    }

    void createTypeVariable(String typeVar) {
        setCurrentNodeTypeRefs(TypeRef.createTypeVarTypeRef(typeVar))
    }

    void dropTypeNode() {
        clearFlags()
        types.pop()
    }

    void startMethodReturnType(voidType) {
        flags.set(METHOD_RESULT)

        if (voidType) {
            setCurrentNodeTypeRefs(TypeRef.createPrimitiveTypeRef("void"))
        }
    }

    void finalizeMethodReturnType() {
        flags.clear(METHOD_RESULT)
    }

    // TODO Fix this to use dimensions
    void setMethodIdentifier(String methodId, String dimensions) {
        methods.peek().setKey(generateMemberKey(methodId))
    }

    def findMethodFromSignature(String signature) {
        Method method = types.peek().findMethodBySignature(signature)
        if (method != null)
            methods.push(method)
        else
            log.warn("Could not find method with sig: ${signature}")

        method
    }

    void createClassNode(String name, int start, int end) {
        setFlag(TYPE_DECL)

        final String fullName = getFullName(name)
        final Class node = Class.builder().compKey(fullName).name(name).start(start).end(end).create()
        types.push(node)
        file.add(node)
    }

    void createEnumNode(String name, int start, int end) {
        setFlag(TYPE_DECL)

        final String fullName = getFullName(name)
        final Enum node = Enum.builder().compKey(fullName).name(name).start(start).end(end).create()
        types.push(node)
        file.add(node)
    }

    void createInterfaceNode(String name, int start, int end) {
        setFlag(TYPE_DECL)

        final String fullName = getFullName(name)
        final Interface node = Interface.builder().compKey(fullName).name(name).start(start).end(end).create()
        types.push(node)
        file.add(node)
    }

    void handleModifiers(String mod, boolean type, boolean field, boolean method) {
        Accessibility access = handleAccessibility(mod)
        Modifier modifier = handleNamedModifiers(mod)

        if (type) {
            if (modifier != null)
                types.peek().addModifier(modifier)
            else if (access != null)
                types.peek().setAccessibility(access)
        }
        else if (field) {
            for (Field node : currentNodes) {
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
                params.peek().addModifier(modifier)
            }
        }
    }

    def handleNamedModifiers(String mod) {
        try {
            return Modifier.forName(mod)
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
        TypeRef ref = TypeRef.createPrimitiveTypeRef(type)
        if (!currentTypeRef.empty() && currentTypeRef.peek().reference == null && currentTypeRef.peek().typeName == null) {
            currentTypeRef.peek().setTypeName(ref.typeName)
            currentTypeRef.peek().setType(TypeRefType.Primitive)
        }
        else
            currentTypeRef.push(ref)

        for (Field node : currentNodes) {
            node.setType(currentTypeRef.peek())
        }
    }

    void createFieldNode(String name, int start, int end) {
        flags.set(FIELD_DECL)
        Field f = Field.builder().compKey(generateMemberKey(name)).start(start).end(end).create()
        currentNodes.add(f)
    }

    void finalizeFieldNodes() {
        for (Field node : currentNodes) {
            types.peek() << node
        }

        currentNodes.clear()
        currentTypeRef.clear()
        clearFlags()
    }

    void createRealization(Type type, String implementsType) {
        Type other = findType(implementsType)
        //if (!(other instanceof UnknownType)) {
        if (other != null)
            proj.addRelation(type, other, RelationType.REALIZATION)
        //}
    }

    void createGeneralization(Type type, String genType) {
        Type other = findType(genType)
        //if (!(other instanceof UnknownType)) {
        if (other != null)
            proj.addRelation(type, other, RelationType.GENERALIZATION)
        //}
    }

    void createContainment(Type type) {
        if (types.size() > 1) {
            proj.addRelation(type, types.get(types.indexOf(type) - 1), RelationType.CONTAINMENT)
        }
    }

    void addUseDependency(String name) {
        Type type = findType(name)
        proj.addRelation(types.peek(), type, RelationType.USE)
    }

    def findStaticInitializer(int num) {
        Initializer init = types.peek().getStaticInitializer(num)
        if (init != null) methods.push(init)
        init
    }

    def findInstanceInitializer(int num) {
        Initializer init = types.peek().getInstanceInitializer(num)
        if (init != null) methods.push(init)
        init
    }
}
