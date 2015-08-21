@echo off

echo "Register automatically some initial users"
echo "Usage: registerusers.bat -c users.txt"
echo ----------------------

subst X: .
X:

set JAVA_HOME=.\runtime\jdk-win32
set PATH=%JAVA_HOME%\bin;%PATH%
set CLASSPATH=./lib/xerces.jar;./lib/xml.jar;./lib/commons-logging.jar;./runtime/tomcat/webapps/wow/lib/wow.jar

java vn.spring.zebra.helperservice.RegisterService -c /vn/spring/zebra/resource/users.txt

@echo on