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

import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Relation extends Model {

    public String getRelKey() {
        return getString("relKey");
    }

    public void setToAndFromRefs(Reference to, Reference from) {
        setToReference(to);
        setFromReference(from);
    }

    public void setToReference(Reference ref) {
        int value = 0;
        if (get("to_id") != null)
            value = getInteger("to_id");
        if (value > 0) {
            removeReference(Reference.findById(value));
        }
        if (ref == null)
            set("to_id", 0);
        else {
            addReference(ref);
            set("to_id", ref.getId());
        }
        save();
    }

    public void setFromReference(Reference ref) {
        int value = 0;
        if (get("from_id") != null)
            value = getInteger("from_id");
        if (value > 0) {
            removeReference(Reference.findById(value));
        }
        if (ref == null)
            set("from_id", 0);
        else {
            addReference(ref);
            set("from_id", ref.getId());
        }
        save();
    }

    private void addReference(Reference ref) {
        add(ref);
        save();
    }

    private void removeReference(Reference ref) {
        remove(ref);
        save();
    }

    public RelationType getType() {
        return RelationType.fromValue(getInteger("type"));
    }

    public void setType(RelationType type) {
        set("type", type.value());
        save();
    }

    public List<Reference> getReferences() {
        return getAll(Reference.class);
    }
}
