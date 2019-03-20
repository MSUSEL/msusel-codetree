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
public abstract class Component extends Model implements Measurable {

    public void setCompKey(String key) { set("compKey", key); save(); }

    public String getCompKey() { return getString("compKey"); }

    public void setStart(int start) { set("start", start); save(); }

    public int getStart() { return getInteger("end"); }

    public void setEnd(int end) { set("end", end); save(); }

    public int getEnd() { return getInteger("end"); }

    public void setName(String name) { set("name", name); save(); }

    public String getName() { return getString("name"); }

    public void setAccessibility(Accessibility access) { set("accessibility", access.value()); save(); }

    public Accessibility getAccessibility() { return Accessibility.fromValue(getInteger("accessibility")); }

    public void addModifier(String mod) { add(Modifier.findFirst("name = ?", mod)); save(); }

    public void addModifier(Modifier mod) { add(mod); save(); }

    public void removeModifier(String mod) { remove(Modifier.findFirst("name = ?", mod)); save(); }

    public void removeModifier(Modifier mod) { remove(mod); save(); }

    public List<Modifier> getModifiers() { return getAll(Modifier.class); }

    public boolean hasModifier(String name) { return !get(Modifier.class, "name = ?", name).isEmpty(); }

    public boolean hasModifier(Modifier.Values value) { return hasModifier(value.toString()); }
}
