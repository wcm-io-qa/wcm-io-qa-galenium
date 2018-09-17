## Galenium API

Galenium is split up into lots of submodules. Each represents part of the Galenium API.

### Core

The fundamental functionality and concepts.

[Read more](io.wcm.qa.galenium.core)

### Selectors

Selectors are a Galenium concept integrating Galen's Locator and Seleniums By APIs.

[Read more](io.wcm.qa.galenium.selectors)

### Differences

Expected differences are central to Galenium's sampling and verification functionality.

[Read more](io.wcm.qa.galenium.differences)

### Interaction

Convenience methods for Selenium interaction with website and browser.

[Read more](io.wcm.qa.galenium.interaction)

### Galen

Galen specific integration and functionality.

[Read more](io.wcm.qa.galenium.galen)

### Galen Specs Maven Plugin

Generate Java classes from Galen specs in a Maven build.

[Read more](io.wcm.qa.galenium.specs-maven-plugin)

### Logging

Logback/SLF4J integration for ExtentReports.

[Read more](io.wcm.qa.galenium.logging)

### Sampling

Sampling is a Galenium concept used heavily in verifications.

[Read more](io.wcm.qa.galenium.sampling)

### Verification

Verifications can handle checks for just about anything.

[Read more](io.wcm.qa.galenium.verification)

### Listeners

Most of Galenium's context and lifecycle management is handled in listeners.

[Read more](io.wcm.qa.galenium.listeners)

### Providers

TestNG providers are a great way to make your tests data driven. This module supplies some implementations and convenience methods to simplify other implementations.

[Read more](io.wcm.qa.galenium.providers)

### Icing

The icing on the cake which integrates functionality from other modules.

[Read more](io.wcm.qa.galenium.icing)

### Integration

To make integration of Galenium into test projects three integration modules are provided.

[Read more](io.wcm.qa.galenium.integration)

#### Integration Parent Galen-Spec

To be used as parent POM for project containing the Galen specs.

[Read more](io.wcm.qa.galenium.integration.specs)

#### Integration Parent UI-Tests

To be used as parent POM for project containing the actual UI tests.

[Read more](io.wcm.qa.galenium.integration.tests)

#### Integration Resources

Contains central resources for Galenium projects to avoid duplication in actual test projects.

[Read more](io.wcm.qa.galenium.integration.resources)

### Examples

As a showcase of what Galenium projects and tests look like, examples are provided.

[Read more](examples/io.wcm.qa.galenium.examples)

#### Example Specs

Some Galen specs set up to generate Java classes from.

[Read more](examples/io.wcm.qa.galenium.examples.specs)

#### Example Tests

Example tests showcasing different Galenium features.

[Read more](examples/io.wcm.qa.galenium.examples.tests)
