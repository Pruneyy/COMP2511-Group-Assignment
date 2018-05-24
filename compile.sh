#!/usr/bin/env bash
set -e

source path.env

mkdir -p $OUT_LOC/src
cp -r src/Images src/Starting\ Board src/sample $OUT_LOC/src
$JAVA_LOC/javac -cp $OUT_LOC/src -d $OUT_LOC/src/ $OUT_LOC/src/sample/*.java
