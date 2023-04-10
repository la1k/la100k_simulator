#!/bin/bash

# Exit on first error
set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd $DIR


export COMPOSE_PROJECT_NAME=uka
if [ -n "$FORCE_BUILD" ]; then
    USER="$(id -u):$(id -g)" docker-compose build --no-cache
fi    

USER="$(id -u):$(id -g)" docker-compose up "$@"
