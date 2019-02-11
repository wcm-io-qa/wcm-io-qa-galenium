#!/bin/bash

SCRIPT_DIR=$(dirname ${0});
echo "changing to SCRIPT_DIR: ${SCRIPT_DIR}";
cd "${SCRIPT_DIR}";

LOCAL_SETTINGS=./local-settings.sh;

# call main script
source ../scripts/maven-call.sh;
