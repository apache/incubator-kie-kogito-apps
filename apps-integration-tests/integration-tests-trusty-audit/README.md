## Trusty AI Console - end to end tests

This project run Trusty AI Console and verify it via Cypress E2E test suites which is located in [UI Packages](../../ui-packages/packages/trusty/it-tests).

#### Requirements

- docker version >= 19.03.12
- java version >= 11
- maven version >= 3.6.3

Note: also previous versions of `docker` might work, but they were not tested.
Note: set [Manage Docker as a non-root user](https://docs.docker.com/engine/install/linux-postinstall/)

#### Perform e2e test suite

The e2e test suite is performed during a build of whole project.

Go to the root folder of this project (kogito-apps) and run:

```
mvn install
```

to build and performed all tests.

In case that you want to run only the E2E test be sure that docker contains all images which are mention in the pom.xml file.

```
docker images
```

Use `mvn install -DskipTests` if you miss any docker image.

Go to this folder (integration-tests-trusty-audit) and perform `mvn install` directly in this module.

#### Clear docker

Remove obsolete image:

```
docker rmi $(docker images 'org.kie.kogito/integration-tests-trusty-service-quarkus' -a -q)
```

Or clear all docker images:

```
docker system prune --all
docker volume prune
docker network rm trusty-nw
```

It seems that docker-maven-plugin does not stop all images so call

```
docker rm -fv $(docker ps -aq)
```
