#!/bin/bash

if [ "$TRAVIS_TAG" != "" ]; then
  echo -e 'Build Branch for Release => Branch ['$TRAVIS_BRANCH']  Tag ['$TRAVIS_TAG']'
  ./gradlew clean test jacocoJunit5TestReport build bintrayUpload -PbintrayUser="${BINTRAY_USER}" -PbintrayKey="${BINTRAY_PASSWORD}" -PdryRun=false --console=plain
else
  echo -e 'Normal Build => Branch ['$TRAVIS_BRANCH']'
  ./gradlew clean test jacocoJunit5TestReport --console=plain
fi