/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
 * Copyright (c) 2015-2017 Montana State University, Gianforte School of Computing,
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
package codetree.node
/**
 * Base class for source code entities.
 *
 * @author Isaac Griffith
 * @version 1.2.0
 */
abstract class CodeNode extends AbstractNode {

    Accessibility accessibility
    def modifiers
    /**
     * The starting line value, only applicable to source code elements
     */
    int start
    /**
     * The ending line value, only applicable to source code elements
     */
    int end

    CodeNode(String key, String parentKey, Map<String, Double> metrics = [:],
             Accessibility accessibility = Accessibility.PUBLIC, modifiers = [],
             int start, int end) {
        super(key, parentKey, metrics)
        this.accessibility = accessibility
        this.modifiers = modifiers
        this.start = start
        this.end = end
    }

    void addModifier(Modifiers mod) {
        if (mod != null && !modifiers.contains(mod))
            modifiers << mod
    }

    void remModifier(Modifiers mod) {
        if (mod != null && modifiers.contains(mod))
            modifiers.remove(mod)
    }
    
    /**
     * Checks whether this line range of this entity contains the provided
     * value.
     *
     * @param line
     *            value to check
     * @return true if the line range of this entity contains the provided line,
     *         false otherwise.
     */
    boolean containsLine(final int line)
    {
        (start..end).contains(line)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String toString()
    {
        StringBuilder builder = new StringBuilder()
        builder.append("CodeNode [start=")
                .append(start)
                .append(", end=")
                .append(end)
                .append(", qIdentifier=")
                .append(key)
                .append(", name=")
                .append(name())
                .append("]")
        return builder.toString()
    }

    boolean hasModifier(Modifiers mod) {
        modifiers.contains(mod)
    }
}
