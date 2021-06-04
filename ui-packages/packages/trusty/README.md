## Trusty AI 

### Install dependencies

To install dependencies you need to have yarn installed globally and run in the terminal:
```
yarn install
```

### Run the project

Compile and run the project in development mode with:
```
yarn run start
```

Open [http://localhost:9000](http://localhost:9000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

### Run project with Docker

#### Requirements

- docker version > 19.03.12
- java version > 11
- maven version > 3.6.3
- docker-compose version > 1.25.2

Note: also previous versions of `docker` and `docker-compose` might work, but they were not tested. 

#### Steps
First, you should be sure that there are no conficts in docker images:
```
docker images
```

Remove obsolete image:
```
docker rmi $(docker images 'org.kie.kogito/integration-tests-trusty-service-quarkus' -a -q)
```

Or clear all docker images:
```
docker system prune --all
docker volume prune
```

Now you need to build all docker images from this kogito project, go to 'kogito-apps' folder
```
mvn clean install -DskipTests
```

Go to the docker-compose folder:
```
cd ui-packages/packages/trusty/docker-compose
```

To be sure that you have built all necessary docker images. Check file docker-compose.yml and output of this command:
```
docker images
```

Run this project:
```
docker-compose up
```

Send REST Request which starts this [DMN asset|https://kiegroup.github.io/kogito-online/?file=https://raw.githubusercontent.com/kiegroup/kogito-apps/master/explainability/explainability-integrationtests/explainability-integrationtests-dmn/src/test/resources/dmn/TrafficViolation.dmn#/editor/dmn]:
```
curl -H "Content-Type: application/json" -X POST -d "{\"Driver\":
{\"State\":\"aa\",\"City\":\"bb\",\"Age\":25,\"Points\":13}
,\"Violation\":{\"Type\":\"speed\",\"Actual Speed\":105,\"Speed Limit\":80}}" http://localhost:8080/Traffic%20Violation
```


Open [http://localhost:1338](http://localhost:1338) to view Trusty Console in the browser.


To stop this project press CTRL+C and run command:
```
docker-compose down
```

### Build the project
```
yarn run build:prod
```
Builds the app for production to the `dist` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.


