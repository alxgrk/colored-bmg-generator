#!/bin/bash

./mvnw clean package -DskipTests

JAVA=$(which java)
[ -z $JAVA ] \
	&& echo "Please install Java and add the JDK location to your PATH system environment variable."

$JAVA -jar target/colored-bmg-generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar
