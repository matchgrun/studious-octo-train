## Build Your Base Container

Build a container as follows.

    docker build --tag=siam-app-base .

Run your container as follows.

    docker run -it siam-app-base bash

This will start your container with a bash shell as a user `developer`. Your
home directory will be `/home/developer`.

A starter set of software has been installed in the `/usr/local` directory
structure.

Your home directory is inside the container. If you exit this container -- use
the `exit` command -- any changes will be lost. Let's address this now.

## Customize Your Container

Create a Docker volume, as follows.

    docker volume create dev-home

This will be used as a volume for your home directory.

Run your container with the attached volume, as follows.

    docker run -v dev-home:/home -it siam-app-base bash

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


