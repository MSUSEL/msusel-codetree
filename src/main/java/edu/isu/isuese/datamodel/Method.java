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
import com.google.common.collect.Sets;
import edu.isu.isuese.datamodel.cfg.ControlFlowGraph;
import lombok.Builder;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

import java.util.List;
import java.util.Set;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsToPolymorphic(parents = {Class.class, Enum.class, Interface.class})
public class Method extends TypedMember {

    protected Method() {

    }

    @Builder(buildMethodName = "create")
    public Method(String name, int start, int end, String compKey, Accessibility accessibility) {
        set("name", name, "start", start, "end", end, "compKey", compKey);
        if (accessibility != null)
            setAccessibility(accessibility);
        else
            setAccessibility(Accessibility.PUBLIC);
        save();
    }

    private ControlFlowGraph cfg = null;

    public void addParameter(Parameter param) { add(param); save(); }

    public void removeParameter(Parameter param) { remove(param); save(); }

    public List<Parameter> getParams() { return getAll(Parameter.class); }

    public void setReturnType(TypeRef ref) { setType(ref); }

    public List<TemplateParam> getTypeParams() { return getAll(TemplateParam.class); }

    public void setTypeParams(List<TemplateParam> params) {
        if (params == null || params.isEmpty())
            return;

        for (TemplateParam param : params)
            add(param);
        save();
    }

    public void addTemplateParam(TemplateParam param) { add(param); save(); }

    public void removeTemplateParam(TemplateParam param) { remove(param); save(); }

    public void addException(TypeRef excep) {
        if (excep != null) {
            // Wrap in MethodException object
            MethodException me = MethodException.createIt();
            me.add(excep);
            me.save();
            add(me);
            save();
        }
    }

    public void removeException(TypeRef excep) {
        if (excep != null) {
            List<MethodException> exceptions = getExceptions();
            MethodException toRemove = null;
            for (MethodException me : exceptions) {
                if (me.getTypeRef().equals(excep))
                    toRemove = me;
            }
            if (toRemove != null) {
                toRemove.removeTypeRef(excep);
                toRemove.save();
                remove(toRemove);
                save();
            }
        }
    }

    public List<MethodException> getExceptions() { return getAll(MethodException.class); }

    public boolean isAbstract() {
        return hasModifier("abstract");
    }

    public boolean isOverriding(Type owner) {
        List<Type> parents = owner.getParentTypes();
        parents.add(owner);
        for(Type t : parents)  {
            if (t.hasMethodWithMatchingSignature(this))
                return true;
        }

        return false;
    }

    public String signature() {
        StringBuilder sig = new StringBuilder();
        sig.append(getType() != null ? getType().getTypeName() + ' ' + getName() : getName());
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

    public Parameter getParameterByName(String param) {
        return get(Parameter.class, "name = ?", param).get(0);
    }

    public TemplateParam getTypeParamByName(String paramName) {
        return get(TemplateParam.class, "name = ?", paramName).get(0);
    }

    public MethodException getExceptionByName(String exception) {
        return get(MethodException.class, "name = ?", exception).get(0);
    }

    public boolean isAccessor() {
        String name = getName();
        List<Parameter> params = getParams();
        return (name.startsWith("get") && params.size() == 0 && (getType() != null && !getType().getTypeName().equals("void")));
    }

    public boolean isMutator() {
        String name = getName();
        List<Parameter> params = getParams();
        return (name.startsWith("set") && params.size() == 1 && (getType() != null && getType().getTypeName().equals("void")));
    }

    public ControlFlowGraph getCfg() {
        if (cfg == null && getId() != null)
            cfg = ControlFlowGraph.fromString(getString("cfg"));
        return cfg;
    }

    public void setCfg(ControlFlowGraph cfg) {
        this.cfg = cfg;
        set("cfg", cfg.cfgToString());
        save();
    }

    public List<Field> getFieldsUsed() {
        return Lists.newArrayList();
    }

    public Set<Field> getFieldsUsedSameClass(Type parent) { return Sets.newHashSet(); }

    public List<Method> getMethodsCalled() {
        return Lists.newArrayList();
    }

    public Set<Method> getMethodsUsedSameClass(Type parent) { return Sets.newHashSet(); }

    public List<Type> getTypesUsing() {
        return Lists.newArrayList();
    }

    public List<Method> getMethodsCalling() {
        return Lists.newArrayList();
    }

    public boolean isVisible() {
        return true;
    }
}
