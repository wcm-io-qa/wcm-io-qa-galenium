# Installing Galenium

To use Galenium, you need a Galenium Maven project.

To set up the project, you can do it from scratch, copy the example projects, or run the Maven Archetype.

Recommended way is to use the Archetype:

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
