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
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;
import org.javalite.activejdbc.annotations.Table;

import java.util.Objects;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Table("refs")
@BelongsToPolymorphic(parents = {Relation.class, Finding.class, Measure.class, RoleBinding.class, TypeRef.class})
public class Reference extends Model {

    public Reference() {}

    public static Reference to(Component comp) {
        RefType type = null;
        if (comp instanceof Type)
            type = RefType.TYPE;
        else if (comp instanceof Literal)
            type = RefType.LITERAL;
        else if (comp instanceof Constructor)
            type = RefType.CONSTRUCTOR;
        else if (comp instanceof Initializer)
            type = RefType.INITIALIZER;
        else if (comp instanceof Method)
            type = RefType.METHOD;
        else if (comp instanceof Field)
            type = RefType.FIELD;

        if (type != null)
            return new Reference(comp.getRefKey(), type);

        return null;
    }


    @Builder(buildMethodName = "create")
    public Reference(String refKey, RefType refType) {
        set("refKey", refKey);
        setType(refType);
        save();
    }

    public String getRefKey() { return getString("refKey"); }

    public RefType getType() { return RefType.fromValue(getInteger("type")); }

    public void setType(RefType type) { set("type", type.value()); save(); }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Reference) {
            Reference ref = (Reference) o;
            return ref.getRefKey().equals(this.getRefKey());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRefKey());
    }

    public Reference copy(String oldPrefix, String newPrefix) {
        return Reference.builder()
                .refKey(this.getRefKey().replace(oldPrefix, newPrefix))
                .refType(this.getType())
                .create();
    }
}
