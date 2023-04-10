#!/bin/bash

# Exit on first error
set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd $DIR

IMAGE_NAME=ghcr.io/lysreklamen/publicpkg/devserver:latest

if [ -n "$FORCE_BUILD" ]; then
    docker build -t "$IMAGE_NAME" .
fi    

if [ -n "$FORCE_PULL" ]; then
    docker pull "$IMAGE_NAME" .
fi    

docker run --rm -it \
    --name "uka_devserver" \
    -u "$(id -u):$(id -g)" \
    -v "$DIR:/app/" \
    -e GRADLE_USER_HOME=/home/user/.gradle \
    -p 127.0.0.1:5678:5678 \
    "$IMAGE_NAME"