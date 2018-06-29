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
package edu.montana.gsoc.msusel.datamodel.environment

import edu.montana.gsoc.msusel.datamodel.DataModelMediator
import edu.montana.gsoc.msusel.datamodel.structural.Namespace
import edu.montana.gsoc.msusel.datamodel.type.Interface
import groovy.util.logging.Slf4j

/**
 * @author Isaac Griffith
 * @version 1.2.0
 */
@Slf4j
abstract class EnvironmentLoader {

    Namespace currentNamespace
    DataModelMediator tree

    EnvironmentLoader(DataModelMediator tree) {
        this.tree = tree
        tree.setLoader(this)
    }

    abstract void find(String ns, String name)

    void find(String ns, InputStream stream, String name) {
        try {
            def type = ~/^(interface|class|enum) (${name})$/
            BufferedReader br = new BufferedReader(new InputStreamReader(stream))

            String line
            while ((line = br.readLine()) != null) {
                def group = line =~ type
                if (group.size() > 0) {
                    println group.size()
                    println group[0]
                    switch (group[0][1]) {
                        case "class":
                            constructClass(group[0][2], ns)
                            break
                        case "interface":
                            constructInterface(group[0][2], ns)
                            break
                        case "enum":
                            constructEnum(group[0][2], ns)
                            break
                    }
                }
            }
        }
        catch (Exception e) {
            log.warn(e.getMessage())
        }
    }

    def constructClass(String name, String pkg) {
        if (currentNamespace == null)
            loadNamespace(pkg)

        Class node = Class.builder()
                .key("${pkg}.${name}")
                .create()

        currentNamespace.addChild(node)
    }

    def constructInterface(String name, String pkg) {
        if (currentNamespace == null)
            loadNamespace(pkg)

        Interface node = Interface.builder()
                .key("${pkg}.${name}")
                .create()

        currentNamespace.addChild(node)
    }

    def constructEnum(String name, String pkg) {
        if (currentNamespace == null)
            loadNamespace(pkg)

        Enum node = Enum.builder()
                .key("${pkg}.${name}")
                .create()

        currentNamespace.addChild(node)
    }

    def loadNamespace(String ns) {
        def space = tree.languageNamespace(ns)
        if (space == null) {
            currentNamespace = Namespace.builder()
                    .key(ns)
                    .create()
            tree.addLanguageNamespace(ns, currentNamespace);
        } else {
            currentNamespace = space
        }
    }

}
