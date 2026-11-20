#!/bin/sh
# Maven Wrapper - works on Linux/Mac
# On Windows, use mvnw.cmd instead

MAVEN_VERSION="3.9.6"
M2_HOME="$HOME/.m2/wrapper/apache-maven-${MAVEN_VERSION}"

if [ ! -d "$M2_HOME" ]; then
  echo "Downloading Apache Maven ${MAVEN_VERSION}..."
  mkdir -p "$HOME/.m2/wrapper"
  curl -sL "https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz" \
    | tar -xz -C "$HOME/.m2/wrapper/"
fi

exec "$M2_HOME/bin/mvn" "$@"
