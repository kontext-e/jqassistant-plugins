jqassistant-plugins
===================
This repository contains plugins for buschmais' [jQAssistant](http://jqassistant.org).

How to install
==============
* add [jQAssistant](http://jqassistant.org) to dependencies in your project (and jgit if git processing is desired).

* declare additional dependencies in your project:
 
    - groupId de.kontext-e.jqassistant.plugin
    - artifactId jqassistant.plugin.checkstyle or .findbugs or .jacoco or .git or .pmd
    - e.g. for gradle:   runtime("de.kontext-e.jqassistant.plugin:jqassistant.plugin.checkstyle:1.3.3")


* you may want to configure some properties in jqassistant.properties or as system property (java -Dpropertyname=propertyvalue):

    - jqassistant.plugin.checkstyle.basepackage=de.kontext_e.jqassistant.plugin
    - jqassistant.plugin.findbugs.filename=findbugs.xml
    - jqassistant.plugin.jacoco.filename=jacoco.xml
    - jqassistant.plugin.pmd.filename=pmd.xml
    - jqassistant.plugin.git.range= - a valid git range if not the complete history should be imported -

* execute jQAssistant tasks

* If you want to add the git plugins to your [jQAssistant](http://jqassistant.org) downloaded commandline installation you currently have to
  manually add the an org.eclipse.jgit jar file to the plugins directory of jqassistant: Use at least version 
  [4.8.0.201706111038-r from Maven Central](https://repo1.maven.org/maven2/org/eclipse/jgit/org.eclipse.jgit/4.8.0.201706111038-r/org.eclipse.jgit-4.8.0.201706111038-r.jar).

Some docs
=========
From master branch. Note: most recent versions from branches may differ.

* [Asciidoc Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/asciidoc/src/main/asciidoc/asciidoc.adoc)
* [Checkstyle Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/checkstyle/src/main/asciidoc/checkstyle.adoc)
* [SpotBugs/FindBugs Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/spotbugs/src/main/asciidoc/spotbugs.adoc)
* [Git Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/git/src/main/asciidoc/git.adoc)
* [JaCoCo Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/jacoco/src/main/asciidoc/jacoco.adoc)
* [JavaParser Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/javaparser/src/main/asciidoc/javaparser.adoc)
* [PlantUML Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/plantuml/src/main/asciidoc/plantuml.adoc)
* [PMD Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/pmd/src/main/asciidoc/pmd.adoc)
* [Excel Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/excel/src/main/asciidoc/excel.adoc)

Latest Changes
==============

Release 1.8.0
-------------
* Use jQAssistant 1.8
* 67 JaCoCo plugins slows down scanning
* 68 Adopt XO related annotations for jQA 1.8
* 70 Upgrade Gradle from v4 to v5

Release 1.7.0
-------------
* Use jQAssistant 1.7
* added a plantuml constraint plantuml:WrongDirection that checks packages with package diagrams
* more properties in PlantUML nodes
* new plugin to import Excel files

Release 1.6.0
-------------
* Use jQAssistant 1.6
* Renamed FindBugs plugin to SpotBugs (still scans FindBugs reports)
* use classpath trick to make long classpath work with Windows
* Upgrade to Gradle 4.10.2
* Upgraded Dependencies to stay compatible with jQAssistant 1.6

Release 1.5.0
-------------
* Use jQAssistant 1.5
* #61 PlantUML jar to version 1.2018.11 to be compabible with main distro
* Simplified Checkstyle plugin graph connection to Java graph; configuration property "jqassistant.plugin.checkstyle.basepackage" is obsolete now
* Own jqa uses Neo4j 3 now
* jacoco: use jqa Java plugins 'SignatureHelper' to create the signature string which replaces own dependency to ASM by dependency to jqa Java plugin; should be more robust and create less trouble
* updated to JavaParser version 3.6.25
* removed some dependencies to other JARs for easier maintenance

Release 1.4.1
-------------
* FindBugs plugin also reads new SpotBugs format (tested with SpotBugs 3.1.3)
* AsciiDoc plugin: relations renamed: HAS_HEADER -> HAS_HEADER_ROW, HAS_BODY -> HAS_ROW, HAS_FOOTER -> HAS_FOOTER_ROW
* AsciiDoc plugin: new rownumber property for :Row nodes
* AsciiDoc plugin: new relation OF_COLUMN from Cell nodes to the column they belong to
* Experimental JavaParser plugin: #58 replaced deprecated GraphDatabaseService by Cypher queries
* #59 Plugins shall migrate existing file nodes instead of creating new ones

Release 1.4.0
-------------
* Relation names changed, see [this blog post](http://techblog.kontext-e.de/jqa-change-relation-names/) for details
* Use jQAssistant 1.4

Release 1.3.3
-------------
* #52 git plugin: protect against multiple scans of the same repo
* #53 PlantUML: scan sequence diagrams
* #54 Try javaparser project to scan also Java sources
* #55 Asciidoc plugin: import also attributes on tables, sections, and lists
* #56 Asciidoc: document an example for defining labels for types matched by regular expressions in tables; see [issue 56](https://github.com/kontext-e/jqassistant-plugins/issues/56)
* #57 PlantUML: Add stereotype property

Release 1.3.2
-------------
* #46 Git Plugin: enhanced the graph of a git repository
* #47 guarded item.getFile() by path.endsWith("/HEAD") to speed up scanning
* #48 For contributors: raised language level to 1.8
* #49 Git Plugin: Add also properties committer, shortMessage, and encoding to GitCommit

Release 1.3.1
-------------
* PlantUML plugin supports Component Diagrams

Release 1.3.0
-------------
* Update to jQAssistant 1.3.0
* Update to jgit 4.8.0.201706111038-r
* Speedup Git "git:CurrentBranch" (thanks Dirk Mahler)
* Added label "Git" to git repository node (thanks Dirk Mahler)


## Compatibility Notes

### v1.5.0

A newer PlantUML jar is now used. This had some API changes. As a consequence, there are some
incompatibilities with former graphs that may affect existing queries:

* :Diagram Property "type" is completely upper cased
* Sequence Diagram labels are in brackets
* Component Diagram is not a :DescriptionDiagram but a :ClassDiagram

## Continuous Build

[![Build Status](https://travis-ci.org/kontext-e/jqassistant-plugins.svg?branch=master)](https://travis-ci.org/kontext-e/jqassistant-plugins)
