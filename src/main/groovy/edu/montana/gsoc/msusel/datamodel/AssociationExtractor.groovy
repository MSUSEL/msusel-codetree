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
package edu.montana.gsoc.msusel.datamodel

import edu.montana.gsoc.msusel.datamodel.member.Field
import edu.montana.gsoc.msusel.datamodel.member.Method
import edu.montana.gsoc.msusel.datamodel.member.Parameter
import edu.montana.gsoc.msusel.datamodel.type.Type

class AssociationExtractor {

    DataModelMediator tree

    AssociationExtractor(DataModelMediator tree) {
        this.tree = tree
    }

    void extractAssociations() {
        Set<Type> types = tree.getTypes()

        types.each { Type type ->
            type.fields().each { Field fld ->
                if (fld.getType() instanceof TypeReference && fld.getType().ref != null) {
                    createAssociation(type, fld.getType())
                }
            }
        }
    }

    void extractDependencies() {
        tree.getSystem().types().each { Type type ->
            type.methods().each { Method meth ->
                if (meth.getType() instanceof TypeReference && meth.getType().ref != null) {
                    createDependency(type, meth.getType())
                }

                meth.params.each { Parameter p ->
                    if (p.getType() instanceof TypeReference && p.getType().ref != null) {
                        createDependency(type, p.getType())
                    }
                }
            }
        }
    }

    private void createAssociation(Type type, TypeReference abref) {
        TypeReference ref = (TypeReference) abref
        Type dep = tree.findType(ref.type)
        tree.addAssociation(type, dep, false)
    }

    private void createDependency(Type type, TypeReference abref) {
        TypeReference ref = (TypeReference) abref
        Type dep = tree.findType(ref.type)
        tree.addDependency(type, dep)
    }
}
