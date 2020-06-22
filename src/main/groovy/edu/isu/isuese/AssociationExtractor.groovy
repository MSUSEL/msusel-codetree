/**
 * The MIT License (MIT)
 *
 * MSUSEL DataModel
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
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

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class AssociationExtractor {

    Project project

    AssociationExtractor(Project proj) {
        this.project = proj
    }

    void extractAssociations() {
        Set<Type> types = project.getAllTypes()

        types.each {
            handleTypeAssociation(it)
        }
    }

    private void handleTypeAssociation(Type type) {
        type.getFields().each { Field f ->
            if (f.getType() != null && f.getType().getReference() != null)
                createAssociation(type, f.getType())
        }
    }

    private void createAssociation(Type type, TypeRef ref) {
        Reference reference = ref.getReference()
        if (reference && ref.getType() == TypeRefType.Type) {
            Type dep = ref.getType(project.getProjectKey())
            type.associatedTo(dep)
        }
    }
}
