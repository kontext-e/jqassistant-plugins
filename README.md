jqassistant-plugins
===================
This repository contains plugins for buschmais' jQAssistant.

How to install
==============
* add [jQAssistant](http://jqassistant.org) to dependencies in your project

* declare additional dependencies in your project:
 
    - groupId de.kontext-e.jqassistant.plugin
    - artifactId jqassistant.plugin.checkstyle or .findbugs or .jacoco or .git

* you may want to configure some properties in jqassistant.properties

    - jqassistant.plugin.checkstyle.basepackage=de.kontext_e.jqassistant.plugin
    - jqassistant.plugin.findbugs.filename=findbugs.xml
    - jqassistant.plugin.jacoco.filename=jacoco.xml

* add a file named 'jqa_plugin_git.properties' to 

* execute jQAssistant tasks

Work in progress
================
* replace jqa_plugin_git.properties
* PlantUML sequence diagrams

Coming soon
===========
* PMD
* Jenkins

## Continuous Build

[![Build Status](https://travis-ci.org/kontext-e/jqassistant-plugins.svg?branch=master)](https://travis-ci.org/kontext-e/jqassistant-plugins)
