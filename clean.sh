#!/usr/bin/env bash

set -e

source path.env

if [[ -n $OUT_LOC ]]
    OUT_LOC=./out
fi

rm -rf $OUT_LOC
