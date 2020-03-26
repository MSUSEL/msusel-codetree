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

import com.google.common.collect.Lists;
import edu.isu.isuese.datamodel.util.DbUtils;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class Member extends Component {

    public List<System> getParentSystems() {
        return DbUtils.getParentSystem(this.getClass(), (Integer) getId());
    }

    public List<Project> getParentProjects() {
        return DbUtils.getParentProject(this.getClass(), (Integer) getId());
    }

    public List<Module> getParentModules() {
        return DbUtils.getParentModule(this.getClass(), (Integer) getId());
    }

    public List<Namespace> getParentNamespaces() {
        return DbUtils.getParentNamespace(this.getClass(), (Integer) getId());
    }

    public List<File> getParentFiles() {
        return DbUtils.getParentFile(this.getClass(), (Integer) getId());
    }

    public List<Type> getParentTypes() {
        List<Type> types = Lists.newLinkedList();
        try {
            types.add(parent(Class.class));
        } catch (IllegalArgumentException e) {}
        try {
            types.add(parent(Enum.class));
        } catch (IllegalArgumentException e) {}
        try {
            types.add(parent(Interface.class));
        } catch (IllegalArgumentException e) {}
        return types;
    }

    @Override
    public String getRefKey() {
        return getString("compKey");
    }

    public void updateKey() {
        Type parent = null;
        try {
            if (parent(Class.class) != null)
                parent = parent(Class.class);
        } catch (IllegalArgumentException e) {
        }
        try {
            if (parent(Interface.class) != null)
                parent = parent(Class.class);
        } catch (IllegalArgumentException e) {
        }
        try {
            if (parent(Enum.class) != null)
                parent = parent(Enum.class);
        } catch (IllegalArgumentException e) {
        }

        String newKey;
        if (parent != null)
            newKey = parent.getCompKey() + "#" + getName();
        else
            newKey = getName();

        setString("compKey", newKey);
        save();
    }
}
