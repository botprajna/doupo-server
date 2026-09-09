$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-8.0.502.7-hotspot"
$env:MAVEN_HOME = "C:\Users\ppp\Tools\apache-maven-3.9.16"
$env:Path = "$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:Path"

Write-Host "JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Cyan
Write-Host "MAVEN_HOME: $env:MAVEN_HOME" -ForegroundColor Cyan
