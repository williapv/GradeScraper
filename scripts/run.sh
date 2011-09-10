#!/bin/bash
script_dir=$(dirname $0)

echo $script_dir
# Absolute path this script is in, thus /home/user/bin
BASEDIR=$script_dir
export JAR_CLASSPATH=$BASEDIR/../target/gradescraper-1.0-SNAPSHOT.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:$BASEDIR/../target/lib/activation-1.1.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:$BASEDIR/../target/lib/commons-codec-1.2.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:$BASEDIR/../target/lib/commons-httpclient-3.0.1.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:$BASEDIR/../target/lib/commons-logging-1.0.3.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:$BASEDIR/../target/lib/mail-1.4.4.jar
echo $JAR_CLASSPATH
java -cp $JAR_CLASSPATH com.williapv.grades.gradescraper.ScrapeGrades
