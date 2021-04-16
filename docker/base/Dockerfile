# Base image
FROM fedora

# Set the working directory to /work
WORKDIR /work

# Create directory structures
COPY ./work /work

# Install software
RUN yum install --assumeyes procps openssh openssh-clients net-tools nc wget git bash-completion zip ansible

# Create a users
ENV DOCKER_USER="developer" DOCKER_GROUP="developer" APP_USER="appadm" APP_NAME="siam-msv-core"

RUN adduser  "${DOCKER_USER}"
RUN adduser  "${APP_USER}"

RUN echo "%${DOCKER_USER} ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/${DOCKER_USER}
RUN echo "%${APP_USER} ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/${APP_USER}

# Install additional SIAM software
RUN /work/setup-base-image.sh ${DOCKER_USER} ${DOCKER_GROUP}

RUN chown -R ${DOCKER_USER}:${DOCKER_GROUP} /work

# Setup this user
USER "${DOCKER_USER}"

WORKDIR /home/${DOCKER_USER}


