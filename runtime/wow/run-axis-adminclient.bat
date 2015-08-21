@echo off
echo "Run Axis Admin Client"

set JAVA_HOME=..\runtime\jdk-win32
set PATH=%JAVA_HOME%\bin;%PATH%
set CLASSPATH=lib/axis.jar;lib/axis-ant.jar;lib/jaxrpc.jar;lib/commons-discovery-0.2.jar;lib/commons-logging-1.0.4.jar;lib/log4j-1.2.8.jar;lib/saaj.jar;lib/wsdl4j-1.5.1.jar;lib/activation.jar;lib/mail_1_4_3.jar;

java org.apache.axis.client.AdminClient -l http://localhost:8080/wow/services/AdminService %*


@echo on