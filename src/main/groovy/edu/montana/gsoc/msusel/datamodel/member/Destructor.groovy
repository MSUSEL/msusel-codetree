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
package edu.montana.gsoc.msusel.datamodel.member

import edu.montana.gsoc.msusel.datamodel.Accessibility
import edu.montana.gsoc.msusel.datamodel.Modifier
import edu.montana.gsoc.msusel.datamodel.TypeReference
import edu.montana.gsoc.msusel.datamodel.type.Type
import groovy.transform.builder.Builder
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Destructor extends Method {

    @Builder(buildMethodName = "create")
    private Destructor(String key, Type parent, String name, Accessibility access, List<Modifier> modifiers, TypeReference type, List<Parameter> params, int start, int end) {
        super(key, parent, name, access, modifiers, type, params, start, end)
    }

    Destructor plus(Parameter p) {
        addParameter(p)
        this
    }

    Destructor minus(Parameter p) {
        removeParameter(p)
        this
    }

    Destructor leftShift(Parameter p) {
        addParameter(p)
        this
    }

    Destructor plus(Modifier m) {
        addModifier(m)
        this
    }

    Destructor leftShift(Modifier m) {
        addModifier(m)
        this
    }

    Destructor minus(Modifier m) {
        removeModifier(m)
        this
    }
}
