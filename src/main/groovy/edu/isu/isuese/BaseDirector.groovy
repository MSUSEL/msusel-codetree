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
package edu.isu.isuese

import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.Project
import org.apache.logging.log4j.Logger

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
abstract class BaseDirector {

    Logger logger
    boolean statements
    Project proj
    ArtifactIdentifier identifier

    BaseDirector(Project proj, ArtifactIdentifier identifier, Logger logger, boolean statements = false) {
        this.proj = proj
        this.identifier = identifier
        this.logger = logger
        this.statements = statements
    }

    void build(String path) {
        List<File> files = identify(proj, path)
        identifier = null
        process(files)
    }

    void identify(Project proj, String path) {
        logger.atInfo().log("Setting up the artifact identifier")
        identifier.setProj(proj)
        identifier.identify(path)

        logger.atInfo().log("Traversing the project and gathering artifact info")
        identifier.getFiles()
        logger.atInfo().log("Artifact info gathering complete")
    }

    void process(final List<File> files) {
        logger.atInfo().log("Processing files and their contained info")
        logger.atInfo().log("Gathering File and Type Info into Model")
        onFiles(files) { File file -> gatherFileAndTypeInfo(file) }
        logger.atInfo().log("Gathering Type Members and Basic Relation Info into Model")
        onFiles(files) { File file -> gatherMembersAndBasicRelationInfo(file) }
        logger.atInfo().log("Gathering Member Usage Info into Model")
        onFiles(files) { File file -> gatherMemberUsageInfo(file) }
        if (statements) {
            logger.atInfo().log("Gathering Statement Info and CFG into Model")
            onFiles(files) { File file -> gatherStatementInfo(file) }
        }
        logger.atInfo().log("Gathering Type Associations into Model")
        gatherTypeAssociations()
        logger.atInfo().log("File processing complete")
    }

    void onFiles(List<File> files, Closure<Void> method) {
        files.each { file ->
            if (includeFile(file)) {
                method(file)
            }
        }
    }

    abstract void gatherFileAndTypeInfo(File file)
    abstract void gatherMembersAndBasicRelationInfo(File file)
    abstract void gatherMemberUsageInfo(File file)
    abstract void gatherStatementInfo(File file)
    abstract boolean includeFile(File file)

    void gatherTypeAssociations() {
        AssociationExtractor assocXtractor = new AssociationExtractor(proj)
        assocXtractor.extractAssociations()
    }
}