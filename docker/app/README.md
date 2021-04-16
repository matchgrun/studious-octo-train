## Build Your Base Container

Execute the following script.

    bin/docker-build.sh

This will compile the application and build a Docker container. This will 
build a container based on the image 'siam-app-base'.

The container is built as follows:

  Create directory 'docker/app/build'
  Copy files from 'docker/app/src' into this build directory.
  Perform the following command.

    docker build --tag=siam-msv-core .


## Test Your Container

Run your container as follows.

    docker run -it siam-msv-core bash

This will start your container with a bash shell as a user `developer`. Your
home directory will be `/home/developer`.

The following directories will be created:

    /applications/bin
    /data/config
    /data/logs

The web service is configured to run on port 9180 inside the container.

Your home directory is inside the container. If you exit this container -- use
the `exit` command -- any changes will be lost. Let's address this now.


## Run Your Container

Run your container, as follows.

    docker run -it siam-msv-core bash

## Customize Your Container

Create a Docker volume, as follows.

    docker volume create dev-home

This will be used as a volume for your home directory.

Run your container with the attached volume, as follows.

    docker run -v dev-home:/home -it siam-msv-core bash

There is a starter copy of the `.bashrc` that you can use. Copy this file to
your home directory.

    cp /work/bashrc ~/.bashrc

Verify that the file works, as follows.

    source ~/.bashrc
    java -version
    jq

All the above commands should produce output for the installed software. Exit
the container and run the container with the attached volume. Issue the
following command.

    java -version

This should produce the output for Java.


## Test Your Container with Web Service

Run your container, as follows.

    docker run -p 3400:9180 -it siam-msv-core bash

Port 3400 (outside the container) is mapped to port 9180 inside the container.

Now, let's start web service inside container.

    $ /applications/bin/start-app.sh

Spring Boot will start, web service will run.

Outside the container, let's test the service.

    curl -s http://localhost:3400/

Test calling one of the web-service methods.

    curl -s http://localhost:3400/v1/attributeDescriptors | jq


## Test Your Container with Web Service

Run your container, as follows.

    docker run -p 3400:9180 -it siam-msv-core /applications/bin/start-app.sh

This will start the service in the container.


## Test Swagger In Your Container

Point your web browser at this endpoint:

  http://localhost:3400/swagger-ui.html



