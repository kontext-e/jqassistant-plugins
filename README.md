jqassistant-plugins
===================
This repository contains plugins for buschmais' [jQAssistant](http://jqassistant.org).

How to install
==============
* If you want to add the git plugins to your [jQAssistant](http://jqassistant.org) installation you currently have to
  manually add the an org.eclipse.jgit jar file to the plugins directory of jqassistant.sh: Use at least version [4.1
  .1 from Maven Central](http://search.maven.org/remotecontent?filepath=org/eclipse/jgit/org.eclipse.jgit/4.1.1
  .201511131810-r/org.eclipse.jgit-4.1.1.201511131810-r.jar).

* add [jQAssistant](http://jqassistant.org) to dependencies in your project (and jgit if git processing is desired).

* declare additional dependencies in your project:
 
    - groupId de.kontext-e.jqassistant.plugin
    - artifactId jqassistant.plugin.checkstyle or .findbugs or .jacoco or .git or .pmd
    - e.g. for gradle:   runtime("de.kontext-e.jqassistant.plugin:jqassistant.plugin.checkstyle:1.0.0")


* you may want to configure some properties in jqassistant.properties or as system property (java -Dpropertyname=propertyvalue):

    - jqassistant.plugin.checkstyle.basepackage=de.kontext_e.jqassistant.plugin
    - jqassistant.plugin.findbugs.filename=findbugs.xml
    - jqassistant.plugin.jacoco.filename=jacoco.xml
    - jqassistant.plugin.pmd.filename=pmd.xml
    - jqassistant.plugin.git.range= - a valid git range if not the complete history should be imported -

* execute jQAssistant tasks

Work in progress
================
* PlantUML sequence diagrams

Coming soon
===========
* Jenkins

## Continuous Build

[![Build Status](https://travis-ci.org/kontext-e/jqassistant-plugins.svg?branch=master)](https://travis-ci.org/kontext-e/jqassistant-plugins)
