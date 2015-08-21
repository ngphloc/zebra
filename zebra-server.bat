@echo off
echo "Start Zebra server through Web server"

subst X: .
X:

set JAVA_HOME=X:\runtime\jdk-win32
set PATH=%JAVA_HOME%\bin;%PATH%
cd .\runtime\tomcat
.\start.bat
cd \

@echo on