Galenium Maven Archetype Project
========================

This project contains a Maven archetype for creating Galenium Projects from scratch.

Run Archetype Project
---------------------

Run the Maven command to add the archetype to your local M2 repository:

`mvn clean install`

Create Galenium Project
------------------------

You can create your Galenium Project with a Maven Command e.g.:

 ```
 mvn archetype:generate 
 -DarchetypeGroupId=io.wcm.qa 
 -DarchetypeArtifactId=io.wcm.qa.galenium.archetype 
 -DarchetypeVersion=0.1.0-SNAPSHOT 
 -DgroupId=de.foo 
 -DartifactId=de.foo.bar 
 -Dversion=1.0-SNAPSHOT 
 -Dpackage=de.foo.bar.sample 
 -DprojectName=MyProject
 ```

