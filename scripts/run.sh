#!/bin/bash
export JAR_CLASSPATH=../target/gradesscraper-1.0-SNAPSHOT.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:../target/lib/activation-1.1.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:../target/lib/commons-codec-1.2.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:../target/lib/commons-httpclient-3.0.1.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:../target/lib/commons-logging-1.0.3.jar
export JAR_CLASSPATH=$JAR_CLASSPATH:../target/lib/mail-1.4.4.jar
echo $JAR_CLASSPATH
java -cp $JAR_CLASSPATH com.williapv.grades.gradesscraper.ScrapeGrades