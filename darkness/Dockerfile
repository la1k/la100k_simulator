#
# Docker container for simplifying developing sequences with darkness.
#
#
# This container intentionally does not contain anything sensitive.
# The purpose of this is to be able to publish this container publicly,
# such that new members of Lysreklamen can get started as quickly as possible
# without any knowledge about docker except installing it.
# 

FROM openjdk:17-slim-buster

# Install python3 from debian repos
RUN apt-get update -y && \
    apt-get install -y python3 python3-pip

# The /app directory will be volume mounted
# to the root folder of this project
# and a homedirectory writeable by anyone and owned by nobody
RUN mkdir /app && mkdir /home/user && chown nobody. /home/user && chmod a+rwx /home/user
WORKDIR /app

# Install the dependencies of pgmplayer.development
RUN python3 -m pip install websockets

# The default entrypoint should simply start the development
# service with no arguments
ENTRYPOINT ["python3", "-m", "pgmplayer.development"]
