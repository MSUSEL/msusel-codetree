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
package edu.isu.isuese.datamodel;

import lombok.Builder;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;
import org.javalite.activejdbc.annotations.Many2Many;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Class.class, Enum.class, Interface.class})
@Many2Many(other = TypeRef.class, join = "constructors_typerefs", sourceFKName = "constructor_id", targetFKName = "type_ref_id")
public class Constructor extends Method {

    public Constructor() {
        super();
    }

    @Builder(buildMethodName = "create", builderMethodName = "creator")
    public Constructor(String name, int start, int end, String compKey, Accessibility accessibility, TypeRef type) {
        super(name, start, end, compKey, accessibility, type);
        save();
    }

    @Override
    public String signature() {
        StringBuilder sig = new StringBuilder();
        sig.append(getName());
        sig.append("(");
        for (Parameter param : getParams()) {
            sig.append(param.getType().getTypeName());
            sig.append(", ");
        }

        String retVal = sig.toString();
        if (retVal.endsWith(", ")) {
            retVal = retVal.trim();
            retVal = retVal.substring(0, retVal.length() - 2);
        }
        retVal += ")";

        return retVal;
    }

    @Override
    public Member copy(String oldPrefix, String newPrefix) {
        Constructor copy = Constructor.creator()
                .name(this.getName())
                .compKey(this.getName())
                .accessibility(this.getAccessibility())
                .type(this.getType().copy(oldPrefix, newPrefix))
                .start(this.getStart())
                .end(this.getEnd())
                .create();

        getModifiers().forEach(copy::addModifier);
        getTemplateParams().forEach(param -> copy.addTemplateParam(param.copy(oldPrefix, newPrefix)));
        getExceptions().forEach(excep -> copy.addException(excep.getTypeRef().copy(oldPrefix, newPrefix)));
        getParams().forEach(param -> copy.addParameter(param.copy()));

        return copy;
    }
}
