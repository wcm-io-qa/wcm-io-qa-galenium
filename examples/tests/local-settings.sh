#!/bin/bash

addGoal clean
addGoal install

addProfile "run-tests"
addDef "galenium.example.sut=jetty"
# addDef "galenium.sampling.verification.ignoreErrors=true"
# Always update snapshots
# addArg "-U";
# Never download dependencies
# addArg "-o";

# Maven debug output
# addArg "-X";

# verbose reporting
addDef "galenium.report.sparse=false"

# addTests "VerificationIT"

# show browser
# addDef "galenium.headless=false"

# run both chrome and FF
# addDef "selenium.browser=chrome,firefox"

addDef "maven.javadoc.skip=true"
