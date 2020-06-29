--
-- The MIT License (MIT)
--
-- MSUSEL DataModel
-- Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
-- Software Engineering Laboratory and Idaho State University, Informatics and
-- Computer Science, Empirical Software Engineering Laboratory
--
-- Permission is hereby granted, free of charge, to any person obtaining a copy
-- of this software and associated documentation files (the "Software"), to deal
-- in the Software without restriction, including without limitation the rights
-- to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
-- copies of the Software, and to permit persons to whom the Software is
-- furnished to do so, subject to the following conditions:
--
-- The above copyright notice and this permission notice shall be included in all
-- copies or substantial portions of the Software.
--
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
-- FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
-- AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
-- LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
-- OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
-- SOFTWARE.
--

create table systems
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(1024),
    basePath   VARCHAR(2048),
    sysKey     VARCHAR(1024),
    created_at DATETIME,
    updated_at DATETIME
);

create table pattern_repositories
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    repoKey    VARCHAR(1024),
    name       VARCHAR(1024),
    created_at DATETIME,
    updated_at DATETIME
);

create table patterns
(
    id                    INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    patternKey            VARCHAR(1024),
    name                  VARCHAR(1024),
    family                VARCHAR(1024),
    pattern_repository_id INTEGER REFERENCES pattern_repositories (id),
    created_at            DATETIME,
    updated_at            DATETIME
);

create table roles
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    roleKey    VARCHAR(1024),
    name       VARCHAR(1024),
    type       INTEGER,
    mandatory  BOOLEAN,
    pattern_id INTEGER REFERENCES patterns (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table roles_role_bindings
(
    id              INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    role_id         INTEGER REFERENCES roles (id),
    role_binding_id INTEGER REFERENCES role_bindings (id),
    created_at      DATETIME,
    updated_at      DATETIME
);

create table relations
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    relKey       VARCHAR(1024),
    project_id   INTEGER,
    reference_id INTEGER,
    to_id        INTEGER,
    from_id      INTEGER,
    type         INTEGER,
    created_at   DATETIME,
    updated_at   DATETIME
);

create table pattern_chains
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    chainKey   VARCHAR(1024),
    system_id  INTEGER REFERENCES systems (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table pattern_instances
(
    id               INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    instKey          VARCHAR(1024),
    pattern_size     INTEGER,
    pattern_chain_id INTEGER REFERENCES pattern_chains (id),
    project_id       INTEGER REFERENCES projects (id),
    pattern_id       INTEGER REFERENCES patterns (id),
    created_at       DATETIME,
    updated_at       DATETIME
);

create table role_bindings
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    pattern_instance_id INTEGER REFERENCES pattern_instances (id),
    created_at          DATETIME,
    updated_at          DATETIME
);

create table refs
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    refKey      VARCHAR(1024),
    type        INTEGER,
    parent_id   INTEGER,
    parent_type VARCHAR(1024),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table findings
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    findingKey   VARCHAR(1024),
    injected     BOOLEAN,
    start        INTEGER,
    end          INTEGER,
    created_at   DATETIME,
    updated_at   DATETIME
);

create table projects_findings
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    project_id INTEGER REFERENCES projects (id),
    finding_id INTEGER REFERENCES findings (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table rules_findings
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    rule_id    INTEGER REFERENCES rules (id),
    finding_id INTEGER REFERENCES findings (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table rules
(
    id                 INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    ruleKey            VARCHAR(1024),
    name               VARCHAR(1024),
    description        VARCHAR(1024),
    priority           INTEGER,
    rule_repository_id INTEGER REFERENCES rule_repositories (id),
    created_at         DATETIME,
    updated_at         DATETIME
);

create table rules_tags
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    rule_id    INTEGER REFERENCES rules (id),
    tag_id     INTEGER REFERENCES tags (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table tags
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    tag        VARCHAR(1024),
    created_at DATETIME,
    updated_at DATETIME
);

create table rule_repositories
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    repoKey    VARCHAR(1024),
    name       VARCHAR(1024),
    created_at DATETIME,
    updated_at DATETIME
);

create table measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    measureKey VARCHAR(1024),
    value      DOUBLE,
    created_at DATETIME,
    updated_at DATETIME
);

create table metrics_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    metric_id  INTEGER REFERENCES metrics (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table projects_measures
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    project_id INTEGER REFERENCES projects (id),
    measure_id INTEGER REFERENCES measures (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table metrics
(
    id                   INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    metricKey            VARCHAR(1024),
    name                 VARCHAR(1024),
    description          VARCHAR(1024),
    handle               VARCHAR(1024),
    evaluator           VARCHAR(1024),
    metric_repository_id INTEGER REFERENCES metric_repositories (id),
    created_at           DATETIME,
    updated_at           DATETIME
);

create table metric_repositories
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    repoKey    VARCHAR(1024),
    name       VARCHAR(1024),
    toolName   VARCHAR(1024),
    created_at DATETIME,
    updated_at DATETIME
);

create table projects
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    projKey    VARCHAR(1024),
    name       VARCHAR(1024),
    version    VARCHAR(1024),
    relPath    VARCHAR(1024),
    srcPath    VARCHAR(2048),
    binPath    VARCHAR(2048),
    testPath   VARCHAR(2048),
    system_id  INTEGER REFERENCES systems (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table languages
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(1024),
    created_at DATETIME,
    updated_at DATETIME
);

create table projects_languages
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    project_id  INTEGER REFERENCES projects (id),
    language_id INTEGER REFERENCES languages (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table modules
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    moduleKey  VARCHAR(1024),
    name       VARCHAR(1024),
    relPath    VARCHAR(1024),
    project_id INTEGER REFERENCES projects (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table scms
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    scmKey     VARCHAR(1024),
    name       VARCHAR(1024),
    tag        VARCHAR(1024),
    branch     VARCHAR(1024),
    url        VARCHAR(1024),
    project_id INTEGER REFERENCES projects (id),
    type       INTEGER,
    created_at DATETIME,
    updated_at DATETIME
);

create table namespaces
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nsKey         VARCHAR(1024),
    name          VARCHAR(1024),
    project_id    INTEGER REFERENCES projects (id),
    relPath       VARCHAR(1024),
    parent_ns_id  INTEGER,
    parent_mod_id INTEGER,
    created_at    DATETIME,
    updated_at    DATETIME
);

create table files
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fileKey      VARCHAR(1024),
    pathIndex    INTEGER NOT NULL,
    name         VARCHAR(1024),
    type         INTEGER,
    relPath      VARCHAR(1024),
    start        INTEGER,
    end          INTEGER,
    parseStage   INTEGER,
    project_id   INTEGER REFERENCES projects (id),
    parent_ns_id INTEGER,
    created_at   DATETIME,
    updated_at   DATETIME
);

create table imports
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(1024),
    start      INTEGER,
    end        INTEGER,
    file_id    INTEGER REFERENCES files (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table unknown_types
(
    id             INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start          INTEGER,
    end            INTEGER,
    compKey        VARCHAR(1024),
    name           VARCHAR(1024),
    accessibility  INTEGER,
    qualified_name VARCHAR(1024),
    project_id     INTEGER REFERENCES projects (id),
    created_at     DATETIME,
    updated_at     DATETIME
);

create table classes
(
    id               INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start            INTEGER,
    end              INTEGER,
    compKey          VARCHAR(1024),
    name             VARCHAR(1024),
    abstract         INTEGER,
    accessibility    INTEGER,
    qualified_name   VARCHAR(1024),
    namespace_id     INTEGER REFERENCES namespaces (id),
    parent_type_id   INTEGER,
    parent_type_type VARCHAR(1024),
    parent_file_id   INTEGER,
    created_at       DATETIME,
    updated_at       DATETIME
);

create table enums
(
    id               INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start            INTEGER,
    end              INTEGER,
    compKey          VARCHAR(1024),
    name             VARCHAR(1024),
    accessibility    INTEGER,
    qualified_name   VARCHAR(1024),
    namespace_id     INTEGER REFERENCES namespaces (id),
    parent_type_id   INTEGER,
    parent_type_type VARCHAR(1024),
    parent_file_id   INTEGER,
    created_at       DATETIME,
    updated_at       DATETIME
);

create table interfaces
(
    id               INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start            INTEGER,
    end              INTEGER,
    compKey          VARCHAR(1024),
    name             VARCHAR(1024),
    accessibility    INTEGER,
    qualified_name   VARCHAR(1024),
    namespace_id     INTEGER REFERENCES namespaces (id),
    parent_type_id   INTEGER,
    parent_type_type VARCHAR(1024),
    parent_file_id   INTEGER,
    created_at       DATETIME,
    updated_at       DATETIME
);

create table literals
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR(1024),
    name          VARCHAR(1024),
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR(1024),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table initializers
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR(1024),
    name          VARCHAR(1024),
    cfg           VARCHAR(2048),
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR(1024),
    number        INTEGER,
    instance      INTEGER(1), -- boolean
    created_at    DATETIME,
    updated_at    DATETIME
);

create table fields
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR(1024),
    name          VARCHAR(1024),
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR(1024),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table methods
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR(1024),
    name          VARCHAR(1024),
    accessibility INTEGER,
    cfg           VARCHAR(2048),
    parent_id     INTEGER,
    parent_type   VARCHAR(1024),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table constructors
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR(1024),
    name          VARCHAR(1024),
    cfg           VARCHAR(2048),
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR(1024),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table destructors
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    start         INTEGER,
    end           INTEGER,
    compKey       VARCHAR(1024),
    name          VARCHAR(1024),
    cfg           VARCHAR(2048),
    accessibility INTEGER,
    parent_id     INTEGER,
    parent_type   VARCHAR(1024),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table parameters
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(1024),
    varg        BOOLEAN,
    parent_id   INTEGER,
    parent_type VARCHAR(1024),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table method_exceptions
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parent_id   INTEGER,
    parent_type VARCHAR(1024),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table methods_method_exceptions
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    method_id           INTEGER REFERENCES methods (id),
    method_exception_id INTEGER REFERENCES method_exceptions (id),
    created_at          DATETIME,
    updated_at          DATETIME
);

create table constructors_method_exceptions
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    constructor_id      INTEGER REFERENCES constructors (id),
    method_exception_id INTEGER REFERENCES method_exceptions (id),
    created_at          DATETIME,
    updated_at          DATETIME
);

create table destructors_method_exceptions
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    destructor_id       INTEGER REFERENCES destructors (id),
    method_exception_id INTEGER REFERENCES method_exceptions (id),
    created_at          DATETIME,
    updated_at          DATETIME
);

create table type_refs
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    dimensions   VARCHAR(1024),
    typeName     VARCHAR(1024),
    typeFullName VARCHAR(1024),
    type         INTEGER,
    typeref_id   INTEGER REFERENCES type_refs (id),
    is_bound     INTEGER(1),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table parameters_typerefs
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parameter_id INTEGER REFERENCES parameters (id),
    type_ref_id  INTEGER REFERENCES type_refs (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table methods_typerefs
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    method_id   INTEGER REFERENCES methods (id),
    type_ref_id INTEGER REFERENCES type_refs (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table constructors_typerefs
(
    id             INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    constructor_id INTEGER REFERENCES constructors (id),
    type_ref_id    INTEGER REFERENCES type_refs (id),
    created_at     DATETIME,
    updated_at     DATETIME
);

create table destructors_typerefs
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    destructor_id INTEGER REFERENCES destructors (id),
    type_ref_id   INTEGER REFERENCES type_refs (id),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table fields_typerefs
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    field_id    INTEGER REFERENCES fields (id),
    type_ref_id INTEGER REFERENCES type_refs (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table methodexceptions_typerefs
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    exception_id INTEGER REFERENCES method_exceptions (id),
    type_ref_id  INTEGER REFERENCES type_refs (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table typerefs_typerefs
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parent_id  INTEGER REFERENCES parameters (id),
    child_id   INTEGER REFERENCES type_refs (id),
    created_at DATETIME,
    updated_at DATETIME
);

create table modifiers
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(1024),
    created_at DATETIME,
    updated_at DATETIME
);

create table classes_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    class_id    INTEGER REFERENCES classes (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table enums_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    enum_id     INTEGER REFERENCES enums (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table interfaces_modifiers
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    interface_id INTEGER REFERENCES interfaces (id),
    modifier_id  INTEGER REFERENCES modifiers (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table literals_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    literal_id  INTEGER REFERENCES literals (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table initializers_modifiers
(
    id             INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    initializer_id INTEGER REFERENCES initializers (id),
    modifier_id    INTEGER REFERENCES modifiers (id),
    created_at     DATETIME,
    updated_at     DATETIME
);

create table fields_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    field_id    INTEGER REFERENCES fields (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table methods_modifiers
(
    id          INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    method_id   INTEGER REFERENCES methods (id),
    modifier_id INTEGER REFERENCES modifiers (id),
    created_at  DATETIME,
    updated_at  DATETIME
);

create table constructors_modifiers
(
    id             INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    constructor_id INTEGER REFERENCES constructors (id),
    modifier_id    INTEGER REFERENCES modifiers (id),
    created_at     DATETIME,
    updated_at     DATETIME
);

create table destructors_modifiers
(
    id            INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    destructor_id INTEGER REFERENCES destructors (id),
    modifier_id   INTEGER REFERENCES modifiers (id),
    created_at    DATETIME,
    updated_at    DATETIME
);

create table parameters_modifiers
(
    id           INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parameter_id INTEGER REFERENCES parameters (id),
    modifier_id  INTEGER REFERENCES modifiers (id),
    created_at   DATETIME,
    updated_at   DATETIME
);

create table template_params
(
    id         INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    created_at DATETIME,
    updated_at DATETIME
);

create table template_params_typerefs
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    template_param_id INTEGER REFERENCES template_params (id),
    typeref_id        INTEGER REFERENCES type_refs (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table methods_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    method_id         INTEGER REFERENCES methods (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table constructors_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    constructor_id    INTEGER REFERENCES constructors (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table destructors_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    destructor_id     INTEGER REFERENCES destructors (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table fields_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    field_id          INTEGER REFERENCES fields (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table interfaces_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    interface_id      INTEGER REFERENCES interfaces (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table classes_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    class_id          INTEGER REFERENCES classes (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

create table enums_template_params
(
    id                INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    enum_id           INTEGER REFERENCES enums (id),
    template_param_id INTEGER REFERENCES template_params (id),
    created_at        DATETIME,
    updated_at        DATETIME
);

insert into modifiers (name)
values ('STATIC'),
       ('FINAL'),
       ('ABSTRACT'),
       ('NATIVE'),
       ('STRICTFP'),
       ('SYNCHRONIZED'),
       ('TRANSIENT'),
       ('VOLATILE'),
       ('DEFAULT'),
       ('ASYNC'),
       ('CONST'),
       ('EXTERN'),
       ('READONLY'),
       ('SEALED'),
       ('UNSAFE'),
       ('VIRTUAL'),
       ('OUT'),
       ('REF'),
       ('PARAMS'),
       ('OVERRIDE'),
       ('NEW'),
       ('PARTIAL'),
       ('EXPLICIT'),
       ('IMPLICIT'),
       ('YIELD'),
       ('THIS');