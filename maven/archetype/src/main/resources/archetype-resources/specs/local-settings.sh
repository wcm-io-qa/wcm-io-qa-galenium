#!/bin/bash

addGoal clean
addGoal install

# Always update snapshots
# addArg "-U";
# Never download dependencies
# addArg "-o";

addDef "maven.javadoc.skip=true"
