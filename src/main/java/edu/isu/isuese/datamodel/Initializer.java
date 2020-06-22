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

import edu.isu.isuese.datamodel.cfg.ControlFlowGraph;
import lombok.Builder;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Class.class, Enum.class, Interface.class})
public class Initializer extends Member {

    public Initializer() {}


    @Builder(buildMethodName = "create")
    public Initializer(String name, int start, int end, String compKey, Accessibility accessibility, boolean instance) {
        set("name", name, "start", start, "end", end, "compKey", compKey, "instance", instance);
        if (accessibility != null)
            setAccessibility(accessibility);
        else
            setAccessibility(Accessibility.PUBLIC);
        save();
    }

    public boolean isInstance() {
        return getBoolean("instance");
    }

    public void setInstance(boolean instance) {
        setBoolean("instance", instance);
        save();
    }

    @Override
    public Member copy(String oldPrefix, String newPrefix) {
        Initializer copy = Initializer.builder()
                .name(this.getName())
                .compKey(this.getName())
                .accessibility(this.getAccessibility())
                .instance(this.isInstance())
                .start(this.getStart())
                .end(this.getEnd())
                .create();

        getModifiers().forEach(copy::addModifier);

        return copy;
    }

    public ControlFlowGraph getCfg() {
        return ControlFlowGraph.fromString(getString("cfg"));
    }

    public void setCfg(ControlFlowGraph cfg) {
        set("cfg", cfg.cfgToString());
        save();
    }
}
