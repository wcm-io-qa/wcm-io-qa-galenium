#!/bin/bash

addGoal clean
addGoal install

addProfile "local-dev"

# Always update snapshots
# addArg "-U";
# Never download dependencies
# addArg "-o";

# verbose reporting
addDef "galenium.report.sparse=false"

# addTests "MyTest"

# show browser
# addDef "galenium.headless=false"

# run both chrome and FF
# addDef "selenium.browser=chrome,firefox"

addDef "maven.javadoc.skip=true"
