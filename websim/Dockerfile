#
# This is a multi-stage docker file which is used both for
# development builds as well as the production version.
#


#
# For development we run two containers
# One for runnning the python backend
# and one for the Javascript frontend
#

FROM python:3.9 as pydev

RUN apt-get update -yy && apt-get install -yy pipenv npm
RUN mkdir /app
WORKDIR /app

ADD Pipfile Pipfile.lock uwsgi.ini ./
RUN pipenv install --system --deploy --ignore-pipfile
ENV FLASK_APP=backend.server FLASK_ENV=development

CMD flask run -h 0.0.0.0 -p 8080

# ------------------------------------------------------------------------------

FROM node:19-bullseye as jsdev

RUN mkdir /app
WORKDIR /app
ADD frontend/package*.json frontend/vue.config.js frontend/

RUN npm install --prefix frontend
CMD npm run --prefix frontend serve

# For development we expect the files to be bindmounted into the containers
# This allows for faster iterations as the container does not have to be rebuilt each time
# we want to test something

# ------------------------------------------------------------------------------

# For production we build the Javascript frontend to get a static distribution output
FROM jsdev as js-staging
COPY frontend frontend
RUN npm run build --prefix frontend

# ------------------------------------------------------------------------------

# For the production container we copy both the backend files and the frontend files into the container image
FROM pydev as prod
COPY backend backend
COPY signs signs
COPY --from=js-staging /app/frontend/dist/ backend/

ENV FLASK_ENV=production
CMD ["uwsgi", "uwsgi.ini"]
