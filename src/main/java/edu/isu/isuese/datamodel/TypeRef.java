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
package edu.isu.isuese.datamodel;

import com.google.common.collect.Lists;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Parameter.class, Method.class, Constructor.class, Destructor.class, Field.class})
@BelongsToParents(
        @BelongsTo(foreignKeyName = "typeref_id", parent = TypeRef.class)
)
public class TypeRef extends Model {

    public String getDimensions() { return getString("dimensions"); }

    public void setDimensions(String dim) { set("dimensions", dim); save(); }

    public String getTypeName() { return getString("typeName"); }

    public void setTypeName(String typeName) { set("typeName", typeName); save(); }

    public TypeRefType getType() { return TypeRefType.fromValue(getInteger("type")); }

    public void setType(TypeRefType type) { set("type", type.value()); save(); }

    public void addBound(TypeRef bound) {
        if (bound != null) {
            add(bound);
            bound.set("is_bound", 1);
            bound.save();
            save();
        }
    }

    public void removeBound(TypeRef bound) {
        if (bound != null) {
            remove(bound);
            bound.set("is_bound", 0);
            bound.save();
            save();
        }
    }

    public List<TypeRef> getBounds() {
        return get(TypeRef.class, "is_bound = ?", 1);
    }

    public void addTypeArg(TypeRef arg) {
        if (arg != null) {
            add(arg);
            arg.set("is_bound", 0);
            arg.save();
            save();
        }
    }

    public void removeTypeArg(TypeRef arg) {
        if (arg != null) {
            remove(arg);
            save();
        }
    }

    public List<TypeRef> getTypeArgs() {
        return get(TypeRef.class, "is_bound = ?", 0);
    }

    public void setReference(Reference ref) {
        List<Reference> refs = Lists.newArrayList(getReferences());
        for (Reference r : refs) {
            removeReference(r);
        }
        if (ref != null)
            add(ref);
        save();
    }

    public void removeReference(Reference ref) { remove(ref); save(); }

    public List<Reference> getReferences() { return getAll(Reference.class); }
}
