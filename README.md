# studious-octo-train

This repository contains the code for a sample project to test toolchain.
Note that running web services will need changes made to the following file(s):
* application.yml
* custom.yml

## Build


To build:

```$ mvn package -D skipTests```

Or better still:

```./mvnw package -D skipTests```

To build with mvnw will require the .mvn directory to be configured. This 
may be done as follows:

```mvn -N io.takari:maven:wrapper```

The directory .mvn SHOULD never be checked in to GIT.

## Running

To run:

```$ ./mvnw spring-boot:run```


