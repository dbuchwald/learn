This is example of Maven project using resources file embedded in JAR file that are filtered during Maven build.

Project contains of simple "Hello world" application in Java that will be compiled and can be executed by manually pointing to executable class. Program will read properties file embedded in JAR file and print out sample variable.

The actual content of the property file will be determined during build phase, and the properties will originate from the following sources:
 * `project.version` - from the standard Maven properties of the project
 * `ext.message` - from the separate properties file (`src/main/filters/filter.properties`)
 * `pom.message` - custom POM.XML property
 * `cl.message` - property passed from command line during build (see build.sh)

Use `build.sh` to compile and `run.sh` to execute code.
