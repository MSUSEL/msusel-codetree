package edu.isu.isuese.datamodel.environment

import edu.isu.isuese.datamodel.Namespace
import groovy.util.logging.Slf4j

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Slf4j
abstract class EnvironmentLoader {

    Namespace currentNamespace

    EnvironmentLoader() {
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
