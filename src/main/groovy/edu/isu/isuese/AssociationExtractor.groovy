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

import edu.isu.isuese.datamodel.*
import org.javalite.activejdbc.ModelListener

class AssociationExtractor {

    Project project

    AssociationExtractor(Project proj) {
        this.project = proj
    }

    void extractAssociations() {
        Set<Type> types = project.getTypes()

        Class.findWith(new ModelListener<Class>() {
            @Override
            void onModel(Class cls) {
                handleTypeAssociation(cls)
            }
        }, "")

        Interface.findWith(new ModelListener<Interface>() {
            @Override
            void onModel(Interface inter) {
                handleTypeAssociation(inter)
            }
        }, "")

        Enum.findWith(new ModelListener<Enum>() {
            @Override
            void onModel(Enum enm) {
                handleTypeAssociation(enm)
            }
        }, "")
    }

    private void handleTypeAssociation(Type type) {
        type.getAll(Field.class).each { Field f ->
            if (f.getType() != null && f.getType().getReference() != null)
                createAssociation(type, f.getType())
        }
    }

    void extractDependencies() {
        Set<Type> types = project.getTypes()

        Class.findWith(new ModelListener<Class>() {
            @Override
            void onModel(Class cls) {
                handleTypeDependency(cls)
            }
        }, "")

        Interface.findWith(new ModelListener<Interface>() {
            @Override
            void onModel(Interface inter) {
                handleTypeDependency(inter)
            }
        }, "")

        Enum.findWith(new ModelListener<Enum>() {
            @Override
            void onModel(Enum enm) {
                handleTypeDependency(enm)
            }
        }, "")
    }

    private void handleTypeDependency(Type type) {
        type.getAll(Method.class).each { Method meth ->
            if (meth.getType() instanceof TypeRef && meth.getType().getReferences().get(0) != null) {
                createDependency(type, meth.getType())
            }

            meth.getParams().each { Parameter p ->
                if (p.getType() instanceof TypeRef && p.getType().getReferences().get(0) != null) {
                    createDependency(type, p.getType())
                }
            }
        }
    }

    private void createAssociation(Type type, TypeRef abref) {
        TypeRef ref = (TypeRef) abref
        Reference reference = ref.getReference()
        if (reference) {
            Type dep = project.findType(reference.getRefKey())
            project.addRelation(type, dep, RelationType.ASSOCIATION)
        }
    }

    private void createDependency(Type type, TypeRef abref) {
        TypeRef ref = (TypeRef) abref
        Reference reference = ref.getReference()
        if (reference) {
            Type dep = project.findType(reference.getRefKey())
            project.addRelation(type, dep, RelationType.DEPENDENCY)
        }
    }
}
