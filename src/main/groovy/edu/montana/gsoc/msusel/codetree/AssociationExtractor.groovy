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

import edu.montana.gsoc.msusel.codetree.node.member.FieldNode
import edu.montana.gsoc.msusel.codetree.node.member.MethodNode
import edu.montana.gsoc.msusel.codetree.node.member.ParameterNode
import edu.montana.gsoc.msusel.codetree.node.type.TypeNode
import edu.montana.gsoc.msusel.codetree.typeref.PrimitiveTypeRef
import edu.montana.gsoc.msusel.codetree.typeref.TypeRef

class AssociationExtractor {

    CodeTree tree

    AssociationExtractor(CodeTree tree) {
        this.tree = tree
    }

    void extractAssociations() {
        Set<TypeNode> types = tree.getUtils().getTypes()

        types.each { TypeNode type ->
            type.fields().each { FieldNode fld ->
                if (fld.getType() instanceof TypeRef && !(fld.getType() instanceof PrimitiveTypeRef)) {
                    createAssociation(type, fld.getType())
                }
            }
        }
    }

    void extractDependencies() {
        tree.getProject().types().each { TypeNode type ->
            type.methods().each { MethodNode meth ->
                if (meth.getType() instanceof TypeRef && !(meth.getType() instanceof PrimitiveTypeRef)) {
                    createDependency(type, meth.getType())
                }

                meth.params.each { ParameterNode p ->
                    if (p.getType() instanceof TypeRef && !(p.getType() instanceof PrimitiveTypeRef)) {
                        createDependency(type, p.getType())
                    }
                }
            }
        }
    }

    private void createAssociation(TypeNode type, AbstractTypeRef abref) {
        TypeRef ref = (TypeRef) abref
        TypeNode dep = tree.getUtils().findType(ref.type)
        tree.addAssociation(type, dep, false)
    }

    private void createDependency(TypeNode type, AbstractTypeRef abref) {
        TypeRef ref = (TypeRef) abref
        TypeNode dep = tree.getUtils().findType(ref.type)
        tree.addDependency(type, dep)
    }
}
