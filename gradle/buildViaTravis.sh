#!/bin/bash

./gradlew clean test jacocoJunit5TestReport build bintrayUpload -PbintrayUser="${BINTRAY_USER}" -PbintrayKey="${BINTRAY_PASSWORD}" -PdryRun=false --console=plain
