#!/bin/bash

mvn clean compile
mvn exec:java -Dexec.mainClass=com.aljovic.amer.Application -Dexec.args="$1"

