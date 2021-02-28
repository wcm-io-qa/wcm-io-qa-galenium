## Galenium API

Galenium is split up into lots of submodules. Each represents part of the Galenium API.

### Core

The fundamental functionality and concepts.

[Read more](modules/core)

### Selectors

Selectors are a Galenium concept integrating Galen's Locator and Seleniums By APIs.

[Read more](modules/selectors)

### Differences

Expected differences are central to Galenium's sampling and verification functionality.

[Read more](modules/differences)

### Interaction

Convenience methods for Selenium interaction with website and browser.

[Read more](modules/interaction)

### Galen

Galen specific integration and functionality.

[Read more](modules/galen)

### Galen Specs Maven Plugin

Generate Java classes from Galen specs in a Maven build.

[Read more](maven/specs-plugin)

### Logging

Logback/SLF4J integration for ExtentReports.

[Read more](modules/logging)

### Sampling

Sampling is a Galenium concept used heavily in verifications.

[Read more](modules/sampling)

### Verification

Verifications can handle checks for just about anything.

[Read more](modules/verification)

### Listeners

Most of Galenium's context and lifecycle management is handled in listeners.

[Read more](modules/listeners)

### Providers

TestNG providers are a great way to make your tests data driven. This module supplies some implementations and convenience methods to simplify other implementations.

[Read more](modules/providers)

### Icing

The icing on the cake which integrates functionality from other modules.

[Read more](modules/icing)

### Integration

To make integration of Galenium into test projects three integration modules are provided.

[Read more](integration)

#### Integration Parent Galen-Spec

To be used as parent POM for project containing the Galen specs.

[Read more](integration/specs)

#### Integration Parent UI-Tests

To be used as parent POM for project containing the actual UI tests.

[Read more](integration/ui-tests)

#### Integration Resources

Contains central resources for Galenium projects to avoid duplication in actual test projects.

[Read more](integration/resources)

### Examples

As a showcase of what Galenium projects and tests look like, examples are provided.

[Read more](examples/examples)

#### Example Specs

Some Galen specs set up to generate Java classes from.

[Read more](examples/specs)

#### Example Tests

Example tests showcasing different Galenium features.

[Read more](examples/tests)
