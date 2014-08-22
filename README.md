jqassistant-plugins
===================
This repository contains plugins for buschmais' jQAssistant.

How to install
==============
For now, that's not quite user friendly. It will get better if jQA is more stable and the latest version
is available at maven repositories.

* get and install jQAssistant (note: M4 is not yet on maven central, so you need to install it in your local maven repo)

* clone this repository

* build the plugins and install them into local maven repo: ./gradlew install

* declare additional dependencies in your project:
 
    - groupId de.kontext-e.jqassistant.plugin
    - artifactId jqassistant.plugin.checkstyle or .findbugs or .jacoco

* execute jQAssistant tasks

Coming soon
===========
* PMD

## Continuous Build

[![Build Status](https://travis-ci.org/jensnerche/jqassistant-plugins.svg?branch=master)](https://travis-ci.org/jensnerche/jqassistant-plugins)
