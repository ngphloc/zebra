@echo off
echo "Run Zebra query client"
echo "GUI usage: run-zebraclient.bat"
echo "Console usage: run-zebraclient.bat -protocol=rmi -host=localhost -type=bayes -userid=root -course=javatutorial -concept=GettingStarted"
echo "Or: run-zebraclient.bat -p rmi -h localhost -t bayes -u root -c javatutorial -concept GettingStarted"
echo ----------------------

set JAVA_HOME=.\runtime\jdk-win32
set PATH=%JAVA_HOME%\bin;%PATH%

set AXIS_CLASSPATH=lib/axis.jar;lib/jaxrpc.jar;lib/commons-discovery-0.2.jar;lib/commons-logging-1.0.4.jar;lib/wsdl4j-1.5.1.jar;lib/activation.jar;lib/mail_1_4_3.jar;

java -cp %AXIS_CLASSPATH%;./runtime/wow/lib/zebra-client.jar vn.spring.zebra.client.TriUMQueryClient %*

@echo on