# Galenium

Selenium Galen Integration

[![Maven Central](https://img.shields.io/maven-central/v/io.wcm.qa/io.wcm.qa.galenium.integration.ui-tests)](https://repo1.maven.org/maven2/io/wcm/qa/io.wcm.qa.galenium.integration.ui-tests/)

## Galenium Documentation

* [Quickstart](quickstart.html): start to use Galenium
* [Background](background.html): learn about the background of the technologies integrated by Galenium
* [Concepts](concepts.html): understand Galenium and its parts better
* [Developing](developing.html): Developing with Galenium
* [Galenium API](apidocs.html): API reference

## Galenium Goals

* Running tests in parallel on multiple servers to save test execution time.
* Running tests on remote browser instances to spread testing load across several machines.
* Running tests for browsers on different platforms or operating systems.
* Running tests for responsive websites with different viewports and breakpoints.
* Getting text and images samples from existing web pages automatically and using samples as a target state for the next test run.

## Galenium's Added Value
 The added value that Galenium generates and why it is worth using Galenium:

* It provides a ready-to-use integration framework with Maven + JUnit + Selenium + Galen + Allure.
* Galenium eliminates typical inhibitors to starting new test automation projects.
  * keeping webdriver versions current
  * initializing expected values
  * scaling tests over different resolutions
* It provides a single point of object definition for Selenium and Galen. Double definition effort is eliminated. The object definition created in Galenium can be used without alteration in standalone Galen. In this case Galen is used without Java and Selenium. This allows to use Galenium object definitions for automated Frontend sanity checks, e.g. for the transition from development test systems to production system.
* Test cases in Galenium have a one-to-one relationship with a test case in Allure. Allure is very test case centric. A clear relationship between test cases in the Maven/JUnit world and in the main report simplifies identifying issues and reacting to them.
