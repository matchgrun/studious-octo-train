# Base image
FROM siam-sot-base

# Set the working directory to /work
WORKDIR /work

# Create directory structures
COPY ./build/*  /work

# Create a user for runtime
ENV DOCKER_USER="developer" DOCKER_GROUP="developer" APP_NAME="studious-octo-train" APP_USER="appadm"

# Install additional SIAM software
RUN /work/setup-app-image.sh ${DOCKER_USER} ${DOCKER_GROUP} ${APP_NAME}

RUN sudo chown -R ${DOCKER_USER}:${DOCKER_GROUP} /work

# Setup this user
USER "${DOCKER_USER}"

WORKDIR /home/${DOCKER_USER}

# Entry point
# CMD "/applications/bin/start-app.sh


