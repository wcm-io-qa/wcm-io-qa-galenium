# Quickstart Guide

## System Requirements

Galenium needs the following to run:

* One of the big three OSs
    * Windows
    * OSX
    * Linux
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


## Installing Galenium

To use Galenium, you need a Galenium Maven project on your local machine.

To set up the project, you can 

1. do it from scratch
1. copy the example projects
1. **run the Maven Archetype**

Recommended way is to use the Archetype.

### Set up using Archetype

You will need a CLI to call the Maven Archetype. In this tutorial we will assume GitBash, but any CLI that runs Maven is fine.

Decide which folder you want to install your Galenium project to. We will assume it is */my/local/dev/folder*.

Switch to the folder. If it does not exist yet, you will have to create it.

```console
user@machine ~
$ cd /my/local/dev/folder
```

##### Archetype interactive

Next execute the Archetype by running the following command:

```console
user@machine /my/local/dev/folder
$ mvn archetype:generate -DarchetypeGroupId=io.wcm.qa -DarchetypeArtifactId=io.wcm.qa.galenium.archetype
```

This will execute the latest release version of Galenium Archetype in interactive mode. You will be asked for missing information like group ID, artifact ID, and so on.

##### Archetype non-interactive

To avoid the interactive mode, you need to specify all information as command line parameters.

```console
user@machine /my/local/dev/folder
$ mvn archetype:generate -DarchetypeGroupId=io.wcm.qa -DarchetypeArtifactId=io.wcm.qa.galenium.archetype -DgroupId=my.group.id -DartifactId=my.artifact.id -Dversion=0.1.0-SNAPSHOT -Dpackage=my.root.packagename -DprojectName=MyTestAutomationProject 
```

The only interaction will be to confirm your choices:

```console
Confirm properties configuration:
groupId: my.group.id
version: 0.1.0-SNAPSHOT
projectName: MyTestAutomationProject
artifactId: my.artifact.id
package: my.root.packagename
 Y: :
```

Just hit enter and the project will be generated.

#### After running Archetype

The Archetype should have set up a multi module build now. 

Which means you should have the following structure in your local folder:

```console
user@machine /my/local/dev/folder
$ ls */*
my.artifact.id/pom.xml

my.artifact.id/parent:
pom.xml

my.artifact.id/specs:
pom.xml  src/

my.artifact.id/tests:
pom.xml  src/
```

To learn more about the modules and what they do you can read more in the [developer's guide](developing.html)

## Running Galenium

From project root run this to compile and run tests:

```console
user@machine /my/local/dev/folder/my.artifact.id
$ mvn clean install -Plocal-dev
```

After the build is done you can find the reports here:

 * ``/my/local/dev/folder/my.artifact.id/tests/target/galenium-reports/extentreports/extentGalen.html``
 * ``/my/local/dev/folder/my.artifact.id/tests/target/galenium-reports/galen/report.html``

