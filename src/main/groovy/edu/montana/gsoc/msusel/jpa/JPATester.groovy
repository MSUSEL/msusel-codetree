/**
 * The MIT License (MIT)
 *
 * MSUSEL CodeTree
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
package edu.montana.gsoc.msusel.jpa

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.persist.PersistService
import com.google.inject.persist.jpa.JpaPersistModule
import edu.montana.gsoc.msusel.dao.PatternDAO
import edu.montana.gsoc.msusel.dao.PatternRepositoryDAO
import edu.montana.gsoc.msusel.datamodel.inject.DAOModule
import edu.montana.gsoc.msusel.datamodel.pattern.Pattern
import edu.montana.gsoc.msusel.datamodel.pattern.PatternRepository

import javax.persistence.Persistence
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class JPATester {

    PersistService service
    @Inject
    PatternRepositoryDAO prdao
    @Inject
    PatternDAO pdao

    @Inject
    JPATester(PersistService service)
    {
        this.service = service
        startService()
    }

    static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new JpaPersistModule("PersistenceUnit2")
                        .properties(DatabaseProduct.MARIA.configuration.configure(null, "root", "c@ffeine10")),
                new DAOModule())

        dropSchema(DatabaseProduct.MARIA.configuration.configure(null, "root", "c@ffeine10"))
        createSchema(DatabaseProduct.MARIA.configuration.configure(null, "root", "c@ffeine10"))
        JPATester tester = injector.getInstance(JPATester.class)
        tester.test()
        tester.stopService()
    }

    void test() {
        String[] names = [
                "Singleton",
                "Factory Method",
                "Prototype",
                "(Object)Adapter",
                "Command",
                "Composite",
                "Decorator",
                "Observer",
                "State",
                "Strategy",
                "Bridge",
                "Template Method",
                "Visitor",
                "Proxy",
                "Proxy2",
                "Chain of Responsibility"
        ]

        PatternRepository repo = PatternRepository.builder()
                .repoKey("GoF")
                .name("Gang of Four Patterns")
                .create()

        repo = prdao.makePersistent(repo)

        for (String n : names) {
            Pattern pattern = Pattern.builder()
                    .repository(repo)
                    .name(n)
                    .create()
            repo << pattern
            pdao.makePersistent(pattern)
        }

        List<Pattern> patterns = pdao.findAll()

        patterns.each { println it }
    }

    private void startService() {
        service.start()
    }

    private void stopService() {
        service.stop()
    }

    static void createSchema(Map<String, String> properties) {
        generateSchema("create", properties)
    }

    static void dropSchema(Map<String, String> properties) {
        generateSchema("drop", properties)
    }

    static void generateSchema(String action, Map<String, String> properties) {
        // Take exiting EMF properties, override the schema generation setting on a copy
        Map<String, String> createSchemaProperties = [:]
        createSchemaProperties += properties
        createSchemaProperties["javax.persistence.schema-generation.database.action"] = action
        Persistence.generateSchema("PersistenceUnit2", createSchemaProperties)
    }
}
