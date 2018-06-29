# MSUSEL  Code Tree

## Installing Maven

This project uses the Maven wrapper so that you do not need to install maven manually.
The first time you go to build this project, simply execute the following command:

```
./mvnw clean install -Dmaven.test.skip=true
```
or for windows:
```
.\mvnw.cmd clean package -Dmaven.test.skip=true
```

## Building

This project can be built using the following command:

```
./mvnw clean package -Dmaven.test.skip=true
```

This project can be compile, tested, or packaged with the following commands:

```
./mvnw clean compile
./mvnw clean test
./mvnw clean package
```

on windows the same commands can be used, but substitute `./mvnw` with `.\mvnw.cmd`

## Deploying
This module is setup to be used by maven as a dependency for several other modules. 
As such it must be deployed to the SparQLine Maven Repository stored on BitBucket.
This can be achieved with the following command (note only perform this on the master branch):

```
./mvnw deploy
```

## Introduction
This module provides the constructs necessary to provide a model of a software project. The basic
component is the `CodeNode` A code type is simply an abstraction of some artifact that occurs within
the software. The `CodeNode` is further refined into serveral subtypes. Containing the entire set of
code nodes is the `CodeTree`. The CodeTree is the datastructure reprsenting the structure of the Project
as a whole.

### Code Tree
The Code Tree provides the means to store the struture of a software project. It's root type, is the
highest-level parent project of a system. 

### Code Node Types
Code Nodes have a set of associated properties including:

* Unique Qualified Identifier
* Name
* Mapping of Metric name to Measurement Value

Beyond this each type of CodeNode has their own properties.

#### Project Node
An abstraction representing the project. Each ProjectNode can contain a set of subprojects, a set of
files, and a set of modules.

#### Namespace Node
An abstraction of a package or namespace. Each PackageNode is associated with a set of types defined
as a part of the package and a set of packages defined as subpackages within this package.

#### File Node
An abstraction representing a file within the project (typically a source code/test code file). Each
file's qualified identifier is its key as defined by some arbitary system, while its name is the absolute
path witin the filesystem it resides in. Files can then define a set of Types which are defined within
the file.

#### Type Node
An abstraction of a type. Currently a type is based on the notion from the Object-Oriented paradigm and
can be either a Class or an Interface. Types can contain a set of Fields and a set of Methods. The unique
qualified name for a type is a combination of its `package.type` name, whereas its name is simply the type name.

#### Field Node
An abstraction of a type variable. Here a field's unique qualified name is a combination of the containing
type's qualified name plus the field's variable name separated by a '#'. A field has several properties including
its associated variable Type and whether or not it is a collection type (i.e., and array or other data structure).

#### Method Node
An abstraction of a type method. A method's unique qualified name is similar to a fields but contains a parenthesized
listing of the parameter types associated with the method. Each method contains the following: a set of parameters,
and a set of statements. A method also has a return type associated with it. Each parameter is simply a pair consisting of
a type and a name.

#### Statement Node
An abstraction of the statements making up the body of a method. A statement has a defined Statement Type
and its unique qualitfied name is a combination of that type and a separate long integer representing the
count of that statement type within the system.