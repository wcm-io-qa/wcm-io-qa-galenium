#!/bin/bash

echo "initializing maven args functions";

function addProfile() {
  addArg "-P${1}";
}

function addTests() {
  for TEST in $@;
  do
    addTest "${TEST}";
  done
}

function addTest() {
  if [ -z "${MRW_TESTS}" ];
  then
    MRW_TESTS="${1}";
  else
    MRW_TESTS="${MRW_TESTS},${1}";
  fi
}

function addGoal() {
  MAVEN_GOALS="${MAVEN_GOALS} ${1}";
}

function addDef() {
  addArg "-D${1}";
}

function addArg() {
  MRW_MAVEN_ARGS="${MRW_MAVEN_ARGS} ${1}";
}

echo "Checking local settings at: '${LOCAL_SETTINGS}'";
if [ -f "${LOCAL_SETTINGS}" ];
then
  echo "using local settings";
  source "${LOCAL_SETTINGS}";
else
  echo "no local settings found";
fi

MRW_MAVEN_CALL="mvn ${MAVEN_GOALS}";
MRW_MAVEN_CALL="${MRW_MAVEN_CALL} ${MRW_MAVEN_ARGS}";
if [ -n "${MRW_TESTS}" ];
then
  MRW_MAVEN_CALL="${MRW_MAVEN_CALL} -Dit.test=${MRW_TESTS} ${@}";
fi

echo "Calling: '${MRW_MAVEN_CALL}'";
${MRW_MAVEN_CALL};