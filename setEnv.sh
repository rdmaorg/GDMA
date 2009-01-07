#!/bin/bash

export MAVEN_HOME=/data/dev/tools/util/maven-2.0.8
export JAVA_HOME=/usr/lib/jvm/java-1.5.0-sun
export ANT_HOME=/data/dev/tools/util/apache-ant-1.7.0
export PATH=${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${ANT_HOME}/bin:${PATH}
export TOMCAT_HOME=/data/dev/tools/servers/apache-tomcat-6.0.16
export MAVEN_OPTs=-Xmx1024m 
# export MAVEN_OPTs=-Xmx1024m -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
