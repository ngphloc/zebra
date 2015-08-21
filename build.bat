@echo off
echo "Update source code"

set JAVA_HOME=.\runtime\jdk-win32
set ANT_HOME=.\runtime\apache-ant
set PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%PATH%

ant

@echo on