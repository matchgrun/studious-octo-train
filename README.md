# siam-msv-core

This repository contains the code for the SIAM Core Microservices project.

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


