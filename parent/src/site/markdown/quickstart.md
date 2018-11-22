# Quickstart Guide

## System Repuirements

Galenium needs the following to run:
* JDK 7+
* Maven 3
* current browser
  * stable or latest Chrome
  * stable or latest Firefox
* free disk space
  * for test code
  * Dependencies downloaded by Maven
  * Reports with Screenshots
  * sample data
* Enough RAM to run Maven build and browser
* One of the big three OSs
  * Windows
  * OSX
  * Linux

## Installing Galenium

To use Galenium, you need a Galenium Maven project.

To set up the project, you can do it from scratch, copy the example projects, or run the Maven Archetype.

TODO: precursor steps, including where to generate project

Recommended way is to use the Archetype:

 ```
 mvn archetype:generate \
 -DarchetypeGroupId=io.wcm.qa \ 
 -DarchetypeArtifactId=io.wcm.qa.galenium.archetype \ 
 -DarchetypeVersion=0.1.0-SNAPSHOT \
 -DgroupId=de.foo \
 -DartifactId=de.foo.bar \ 
 -Dversion=1.0-SNAPSHOT \
 -Dpackage=de.foo.bar.sample \ 
 -DprojectName=MyProject 
 ```

 TODO: expected results (have three folders, parent/specs/tests)

## Running Galenium

TODO: Describe running example from archetype
 
## Developing with Galenium

### Step-by-Step Procedure

* Define objects
* Write Selenium tests
* Add Galen specifications
* Generate sample data for target state if applicable
* Run tests
* Compare with sampled target state if applicable
* Check reports

### Information Sources

* Software configuration management (SCM)
  * Galenium tests
  * Galen specifications
  * Galen test suites
* Continous Integration (CI)/Jenkins
  * Jobs
  * Reports
* Selenium Grid
  * Browsers
  * OSs
 