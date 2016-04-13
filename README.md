jqassistant-plugins
===================
This repository contains plugins for buschmais' [jQAssistant](http://jqassistant.org).

How to install
==============
* If you want to add the git plugins to your [jQAssistant](http://jqassistant.org) installation you currently have to
  manually add the an org.eclipse.jgit jar file to the plugins directory of jqassistant: Use at least version 
  [4.1.1 from Maven Central](http://search.maven.org/remotecontent?filepath=org/eclipse/jgit/org.eclipse.jgit/4.1.1.201511131810-r/org.eclipse.jgit-4.1.1.201511131810-r.jar).

* add [jQAssistant](http://jqassistant.org) to dependencies in your project (and jgit if git processing is desired).

* declare additional dependencies in your project:
 
    - groupId de.kontext-e.jqassistant.plugin
    - artifactId jqassistant.plugin.checkstyle or .findbugs or .jacoco or .git or .pmd
    - e.g. for gradle:   runtime("de.kontext-e.jqassistant.plugin:jqassistant.plugin.checkstyle:1.1.0")


* you may want to configure some properties in jqassistant.properties or as system property (java -Dpropertyname=propertyvalue):

    - jqassistant.plugin.checkstyle.basepackage=de.kontext_e.jqassistant.plugin
    - jqassistant.plugin.findbugs.filename=findbugs.xml
    - jqassistant.plugin.jacoco.filename=jacoco.xml
    - jqassistant.plugin.pmd.filename=pmd.xml
    - jqassistant.plugin.git.range= - a valid git range if not the complete history should be imported -

* execute jQAssistant tasks

Some docs
=========
From master branch. Note: most recent versions from branches may differ.

* [Checkstyle Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/checkstyle/src/main/asciidoc/checkstyle.adoc)
* [FindBugs Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/findbugs/src/main/asciidoc/findbugs.adoc)
* [Git Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/git/src/main/asciidoc/git.adoc)
* [JaCoCo Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/jacoco/src/main/asciidoc/jacoco.adoc)
* [PlantUML Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/plantuml/src/main/asciidoc/plantuml.adoc)
* [PMD Plugin](https://github.com/kontext-e/jqassistant-plugins/blob/master/pmd/src/main/asciidoc/pmd.adoc)

Work in progress
================
* PlantUML class diagram packages; please note: although the plantuml plugin is version 1.1.0, it is
  far from a complete PlantUML import; in fact, only the packages of class diagrams were imported for now
  because this is the only thing that is currently needed for architecture rules

Coming later
============
* Jenkins

## Continuous Build

[![Build Status](https://travis-ci.org/kontext-e/jqassistant-plugins.svg?branch=master)](https://travis-ci.org/kontext-e/jqassistant-plugins)
