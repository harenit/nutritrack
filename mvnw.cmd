@echo off
set MAVEN_VERSION=3.9.6
set M2_HOME=%USERPROFILE%\.m2\wrapper\apache-maven-%MAVEN_VERSION%

if not exist "%M2_HOME%\bin\mvn.cmd" (
  echo Downloading Apache Maven %MAVEN_VERSION%...
  powershell -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://downloads.apache.org/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile '%TEMP%\maven.zip' }"
  powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%USERPROFILE%\.m2\wrapper\' -Force"
)

"%M2_HOME%\bin\mvn.cmd" %*
