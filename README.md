# LatestFromGithubLaunch
A simple Java application that will grab the latest binary and launch it using the same JVM runnung the updater jar. an new JVM instance is spawned to avoid problems of classpath and reflection that come form the dynamic launchers such as update4j.

# Usage

LatestFromGithubLaunch is a command line utility. You pass in the project, repository, artifact name and runtime information.

```
java -jar LatestFromGithubLaunch.jar CommonWealthRobotics BowlerStudio BowlerStudio.jar /home/hephaestus/bin/java8/bin/java -jar
```

# Bash Example

the file
```
bowlerstudio
```
is a Bash implementation using Zenity. It works perfectly on Lunux but is not portable. For a lighter version of this application, look at that BASH script

