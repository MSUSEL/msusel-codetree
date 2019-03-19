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
package edu.isu.isuese.datamodel.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.isu.isuese.datamodel.Class;
import edu.isu.isuese.datamodel.*;
import edu.isu.isuese.datamodel.Enum;
import edu.isu.isuese.datamodel.System;
import org.apache.commons.lang3.tuple.Pair;
import org.javalite.activejdbc.Association;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.associations.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class DbUtils {

    private Map<String, String> properties;
    private boolean open = false;

    public void loadProperties(Map<String, String> properties) throws DbUtilsException {
        if (verifyProperties(properties))
            this.properties = properties;
        else
            throw new DbUtilsException("Missing Database Property");
    }

    private boolean verifyProperties(Map<String, String> properties) {
        return properties.containsKey(Constants.DB_DRIVER_KEY) &&
                properties.containsKey(Constants.DB_URL_KEY) &&
                properties.containsKey(Constants.DB_USER_KEY) &&
                properties.containsKey(Constants.DB_PASS_KEY);
    }

    public void openDbConnection() throws DbUtilsException {
        if (properties != null) {
            Base.open(properties.get(Constants.DB_DRIVER_KEY),
                    properties.get(Constants.DB_URL_KEY),
                    properties.get(Constants.DB_USER_KEY),
                    properties.get(Constants.DB_PASS_KEY));
            open = true;
        } else {
            throw new DbUtilsException("Database cannot be opened until Database connection properties are loaded.");
        }
    }

    public void closeDbConnection() throws DbUtilsException {
        if (open)
            Base.close();
        else
            throw new DbUtilsException("Cannot close a database connection that was never opened.");
    }

    public static String createAscendingJoin(java.lang.Class<? extends Model> model, java.lang.Class<? extends Model> goal, int id, Filter... filters)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Pair<java.lang.Class<? extends Model>, String>> path = extractJoinPath(model, goal, true);

        String goalTblName = getTableNameReflect(goal);

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("SELECT * FROM %s ", goalTblName));
        for (int i = path.size() - 1; i > 0; i--) {
            String leftTblName = getTableNameReflect(path.get(i - 1).getLeft());
            String rightTblName = getTableNameReflect(path.get(i).getLeft());
            String fkey = path.get(i - 1).getRight();
//            String fkey = "";
            builder.append(String.format("JOIN %s ON %s.id = %s.%s ", leftTblName, rightTblName, leftTblName, fkey));
        }
        String leftTblName = getTableNameReflect(path.get(0).getLeft());
        String rightTblName = getTableNameReflect(path.get(1).getLeft());
        builder.append(String.format("AND %s.id = %d", leftTblName, id));

        appendFilters(builder, filters);

        return builder.toString();
    }

    public static String createDescendingJoin(java.lang.Class<? extends Model> model, java.lang.Class<? extends Model> goal, int id, Filter... filters)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Pair<java.lang.Class<? extends Model>, String>> path = extractJoinPath(model, goal, false);

        String goalTblName = getTableNameReflect(goal);

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("SELECT * FROM %s ", goalTblName));
        for (int i = path.size() - 1; i > 1; i--) {
            String leftTblName = getTableNameReflect(path.get(i - 1).getLeft());
            String rightTblName = getTableNameReflect(path.get(i).getLeft());
            String fkey = path.get(i - 1).getRight();
            if (fkey.startsWith("M2M:")) {
                fkey = fkey.split(":")[1];
                String[] components = fkey.split(","); // 0 = join_table, 1 = source_id, 2 = target_id
                builder.append(String.format("JOIN %s ON %s.id = %s.%s ", components[0], rightTblName, components[0], components[1]));
                builder.append(String.format("JOIN %s ON %s.%s = %s.id ", leftTblName, components[0], components[1], leftTblName));
            } else {
                builder.append(String.format("JOIN %s ON %s.%s = %s.id ", leftTblName, rightTblName, fkey, leftTblName));
            }
        }
        String leftTblName = getTableNameReflect(path.get(0).getLeft());
        String rightTblName = getTableNameReflect(path.get(1).getLeft());
        String fkey = path.get(0).getRight();
//        String fkey = "";
        builder.append(String.format("JOIN %s ON %s.%s = %d", leftTblName, rightTblName, fkey, id));

        appendFilters(builder, filters);

        return builder.toString();
    }

    private static void appendFilters(StringBuilder builder, Filter... filters) {
        if (filters.length > 0) {
            builder.append("WHERE ");
            for (Filter f : filters) {
                builder.append(f.toString());
                builder.append(" AND ");
            }
            int x = builder.lastIndexOf(" AND ");
            builder.delete(x, builder.length() - 1);
        }
        builder.append(";");
    }

    private static String getTableNameReflect(java.lang.Class<? extends Model> model) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return (String) model.getMethod("getTableName").invoke(null);
    }

    private static List<Pair<java.lang.Class<? extends Model>, String>> extractJoinPath(java.lang.Class<? extends Model> model, java.lang.Class<? extends Model> goal, boolean ascending) {
        List<java.lang.Class<? extends Model>> visited = Lists.newArrayList();
        Queue<java.lang.Class<? extends Model>> queue = Lists.newLinkedList();
        queue.offer(model);
        java.lang.Class<? extends Model> current = model;
        Map<java.lang.Class<? extends Model>, Pair<java.lang.Class<? extends Model>, String>> meta = Maps.newHashMap();
        meta.put(current, null);

        while (!queue.isEmpty()) {
            try {
                current = queue.poll();
                if (current.getCanonicalName().equals(goal.getCanonicalName())) {
                    return extractPath(current, meta);
                }

                List<Pair<java.lang.Class<? extends Model>, String>> succ;
                if (ascending)
                    succ = buildAscendingSuccessorList(current);
                else
                    succ = buildDescendingSuccessorList(current);

                visited.add(current);

                for (Pair<java.lang.Class<? extends Model>, String> c : succ) {
                    if (visited.contains(c.getLeft()) || c.getLeft() == current) {
                        continue;
                    }

                    if (!queue.contains(c)) {
                        //meta.put(c.getLeft(), Pair.of(current, c.getRight()));
                        meta.put(c.getLeft(), Pair.of(current, c.getRight()));
                        queue.offer(c.getLeft());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return extractPath(current, meta);
    }

    private static List<Pair<java.lang.Class<? extends Model>, String>> extractPath(java.lang.Class<? extends Model> source, Map<java.lang.Class<? extends Model>, Pair<java.lang.Class<? extends Model>, String>> meta) {
        List<Pair<java.lang.Class<? extends Model>, String>> list = Lists.newLinkedList();

        list.add(Pair.of(source, null));
        while (meta.get(source) != null) {
            list.add(meta.get(source));
            source = meta.get(source).getLeft();
        }

        Collections.reverse(list);

        return list;
    }

    private static List<Pair<java.lang.Class<? extends Model>, String>> buildDescendingSuccessorList(java.lang.Class<? extends Model> model)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Association> assocs = Lists.newArrayList((List<Association>) model.getMethod("associations").invoke(null));
        List<Pair<java.lang.Class<? extends Model>, String>> succ = Lists.newArrayList();

        boolean omaOrM2m = false;
        for (Association a : assocs) {
            if (a instanceof OneToManyAssociation) {
                omaOrM2m = true;
                break;
            }
        }

        for (Association ass : assocs) {
            if (ass instanceof OneToManyAssociation) {
                OneToManyAssociation oma = (OneToManyAssociation) ass;
                succ.add(Pair.of(oma.getTargetClass(), oma.getFkName()));
            } else if (ass instanceof OneToManyPolymorphicAssociation && !omaOrM2m) {
                OneToManyPolymorphicAssociation oma = (OneToManyPolymorphicAssociation) ass;
                succ.add(Pair.of(oma.getTargetClass(), "parent_id"));
            } else if (ass instanceof Many2ManyAssociation) {
                Many2ManyAssociation m2m = (Many2ManyAssociation) ass;
                succ.add(Pair.of(m2m.getTargetClass(), String.format("M2M:%s,%s,%s", m2m.getJoin(), m2m.getTargetFkName(), m2m.getSourceFkName())));
            }
        }

        return succ;
    }

    private static List<Pair<java.lang.Class<? extends Model>, String>> buildAscendingSuccessorList(java.lang.Class<? extends Model> model)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Association> assocs = (List<Association>) model.getMethod("associations").invoke(null);
        List<Pair<java.lang.Class<? extends Model>, String>> succ = Lists.newArrayList();
        for (Association ass : assocs) {
            if (ass instanceof BelongsToAssociation) {
                BelongsToAssociation bta = (BelongsToAssociation) ass;
                succ.add(Pair.of(bta.getTargetClass(), bta.getFkName()));
            } else if (ass instanceof BelongsToPolymorphicAssociation) {
                BelongsToPolymorphicAssociation bta = (BelongsToPolymorphicAssociation) ass;
                succ.add(Pair.of(bta.getTargetClass(), "parent_id"));
            }
        }
        return succ;
    }

    public static List<SCM> getSCMs(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return SCM.findBySQL(DbUtils.createDescendingJoin(model, SCM.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Module> getModules(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Module.findBySQL(DbUtils.createDescendingJoin(model, Module.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Namespace> getNamespaces(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Namespace.findBySQL(DbUtils.createDescendingJoin(model, Namespace.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<File> getFiles(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return File.findBySQL(DbUtils.createDescendingJoin(model, File.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Import> getImports(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Import.findBySQL(DbUtils.createDescendingJoin(model, Import.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Type> getTypes(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        List<Type> types = Lists.newLinkedList();
        types.addAll(getClasses(model, id, filters));
        types.addAll(getEnums(model, id, filters));
        types.addAll(getInterfaces(model, id, filters));
        return types;
    }

    public static List<edu.isu.isuese.datamodel.Class> getClasses(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return edu.isu.isuese.datamodel.Class.findBySQL(DbUtils.createDescendingJoin(model, edu.isu.isuese.datamodel.Class.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Interface> getInterfaces(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Interface.findBySQL(DbUtils.createDescendingJoin(model, Interface.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Enum> getEnums(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Enum.findBySQL(DbUtils.createDescendingJoin(model, Enum.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Member> getMembers(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        List<Member> members = Lists.newLinkedList();

        members.addAll(getLiterals(model, id, filters));
        members.addAll(getInitializers(model, id, filters));
        members.addAll(getTypedMembers(model, id, filters));

        return members;
    }

    public static List<Literal> getLiterals(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            List<Type> types = getTypes(model, id, filters);
            List<Literal> consts = Lists.newArrayList();
            for (Type t : types) {
                consts.addAll(t.getLiterals());
            }
            return consts;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Initializer> getInitializers(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            List<Type> types = getTypes(model, id, filters);
            List<Initializer> consts = Lists.newArrayList();
            for (Type t : types) {
                consts.addAll(t.getInitializers());
            }
            return consts;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<TypedMember> getTypedMembers(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        List<TypedMember> members = Lists.newLinkedList();

        members.addAll(getFields(model, id, filters));
        members.addAll(getAllMethods(model, id, filters));

        return members;
    }

    public static List<Field> getFields(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            List<Type> types = getTypes(model, id, filters);
            List<Field> consts = Lists.newArrayList();
            for (Type t : types) {
                consts.addAll(t.getFields());
            }
            return consts;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Method> getAllMethods(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        List<Method> methods = Lists.newLinkedList();

        methods.addAll(getMethods(model, id, filters));
        methods.addAll(getConstructors(model, id, filters));
        methods.addAll(getDestructors(model, id, filters));

        return methods;
    }

    public static List<Method> getMethods(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            List<Type> types = getTypes(model, id, filters);
            List<Method> consts = Lists.newArrayList();
            for (Type t : types) {
                consts.addAll(t.getMethods());
            }
            return consts;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Constructor> getConstructors(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            List<Type> types = getTypes(model, id, filters);
            List<Constructor> consts = Lists.newArrayList();
            for (Type t : types) {
                consts.addAll(t.getConstructor());
            }
            return consts;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Destructor> getDestructors(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            List<Type> types = getTypes(model, id, filters);
            List<Destructor> consts = Lists.newArrayList();
            for (Type t : types) {
                consts.addAll(t.getDestructors());
            }
            return consts;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<PatternInstance> getPatternInstances(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return PatternInstance.findBySQL(DbUtils.createDescendingJoin(model, PatternInstance.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Language> getLanguages(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Language.findBySQL(DbUtils.createDescendingJoin(model, Language.class, id, filters));
        } catch (Exception e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }
    }

    public static List<Measure> getMeasures(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Measure.findBySQL(DbUtils.createDescendingJoin(model, Measure.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<RoleBinding> getRoleBindings(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            List<PatternInstance> patterns = PatternInstance.findBySQL(DbUtils.createDescendingJoin(model, PatternInstance.class, id, filters));
            List<RoleBinding> bindings = Lists.newLinkedList();

            java.lang.System.out.println("Num Instances: " + patterns.size());
            for (PatternInstance p : patterns) {
                bindings.addAll(RoleBinding.findBySQL(DbUtils.createDescendingJoin(PatternInstance.class, RoleBinding.class, (Integer) p.getId(), filters)));
            }
            return bindings;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Finding> getFindings(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Finding.findBySQL(DbUtils.createDescendingJoin(model, Finding.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Relation> getRelations(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Relation.findBySQL(DbUtils.createDescendingJoin(model, Relation.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Tag> getTags(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Tag.findBySQL(DbUtils.createDescendingJoin(model, Tag.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Role> getRoles(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            List<PatternInstance> patterns = PatternInstance.findBySQL(DbUtils.createDescendingJoin(model, PatternInstance.class, id, filters));
            List<Role> roles = Lists.newLinkedList();

            for (PatternInstance p : patterns) {
                roles.addAll(Role.findBySQL(DbUtils.createDescendingJoin(PatternInstance.class, Role.class, (Integer) p.getId(), filters)));
            }
            return roles;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<System> getParentSystem(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return System.findBySQL(DbUtils.createAscendingJoin(model, System.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Project> getParentProject(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Project.findBySQL(DbUtils.createAscendingJoin(model, Project.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Module> getParentModule(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Module.findBySQL(DbUtils.createAscendingJoin(model, Module.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Namespace> getParentNamespace(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Namespace.findBySQL(DbUtils.createAscendingJoin(model, Namespace.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<File> getParentFile(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return File.findBySQL(DbUtils.createAscendingJoin(model, File.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Type> getParentType(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        List<Type> types = Lists.newLinkedList();

        types.addAll(getParentClass(model, id, filters));
        types.addAll(getParentEnum(model, id, filters));
        types.addAll(getParentInterface(model, id, filters));

        return types;
    }

    public static List<Class> getParentClass(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Class.findBySQL(DbUtils.createAscendingJoin(model, Class.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Enum> getParentEnum(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Enum.findBySQL(DbUtils.createAscendingJoin(model, Enum.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Interface> getParentInterface(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Interface.findBySQL(DbUtils.createAscendingJoin(model, Interface.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Method> getParentAllMethod(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        List<Method> methods = Lists.newLinkedList();

        methods.addAll(getParentMethod(model, id, filters));
        methods.addAll(getParentConstructor(model, id, filters));
        methods.addAll(getParentDestructor(model, id, filters));

        return methods;
    }

    public static List<Method> getParentMethod(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Method.findBySQL(DbUtils.createAscendingJoin(model, Method.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Constructor> getParentConstructor(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Constructor.findBySQL(DbUtils.createAscendingJoin(model, Constructor.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Destructor> getParentDestructor(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Destructor.findBySQL(DbUtils.createAscendingJoin(model, Destructor.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<PatternChain> getParentPatternChain(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return PatternChain.findBySQL(DbUtils.createAscendingJoin(model, PatternChain.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<PatternRepository> getParentPatternRepository(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return PatternRepository.findBySQL(DbUtils.createAscendingJoin(model, PatternRepository.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<PatternInstance> getParentPatternInstance(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return PatternInstance.findBySQL(DbUtils.createAscendingJoin(model, PatternInstance.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<RoleBinding> getParentRoleBinding(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return RoleBinding.findBySQL(DbUtils.createAscendingJoin(model, RoleBinding.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<MetricRepository> getParentMetricRepository(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return MetricRepository.findBySQL(DbUtils.createAscendingJoin(model, MetricRepository.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Metric> getParentMetric(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Metric.findBySQL(DbUtils.createAscendingJoin(model, Metric.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<RuleRepository> getParentRuleRepository(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return RuleRepository.findBySQL(DbUtils.createAscendingJoin(model, RuleRepository.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Rule> getParentRule(java.lang.Class<? extends Model> model, int id, Filter... filters) {
        try {
            return Rule.findBySQL(DbUtils.createAscendingJoin(model, Rule.class, id, filters));
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Type> getRelationTo(Type t, RelationType relType) {
        try {
            return Type.findBySQL("SELECT * FROM types t" +
                    " JOIN refs r on r.refkey = t.key" +
                    " JOIN relations rel on rel.to_id = r.id AND rel.type = ?" +
                    " JOIN refs s on s.id = rel.from_id" +
                    " JOIN types x on x.key = s.refKey;");
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static List<Type> getRelationFrom(Type t, RelationType relType) {
        try {
            return Type.findBySQL("SELECT * FROM types t" +
                    " JOIN refs r on refs.refKey = t.key" +
                    " JOIN relations rel on rel.from_id = r.id AND rel.type = ?" +
                    " JOIN refs s on s.id = rel.to_id" +
                    " JOIN types x on x.key = s.refKey;");
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }
}
