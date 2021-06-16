#!/bin/bash
KOGITO_APPS=''
if [[ "$PWD" == */docker-compose ]]
then 
    KOGITO_APPS='../../../../..'
    echo 'script runs from the docker-compose folder'
elif [[ "$PWD" == */packages/trusty ]]
then 
    KOGITO_APPS='../../../'
    echo "what"
    echo $KOGITO_APPS
    echo 'script runs from the trusty folder'
else
    >&2 echo "error: script starts from unexpected location: ${PWD}"
    >&2 echo "error: script expects /ui-packages/packages/trusty or /ui-packages/packages/trusty/it-tests/docker-compose folders"
    exit
fi

docker rmi $(docker images 'org.kie.kogito/integration-tests-trusty-service-quarkus' -a -q)
docker rmi $(docker images 'org.kie.kogito/trusty-service-infinispan' -a -q)
docker rmi $(docker images 'org.kie.kogito/explainability-service-messaging' -a -q)
docker rmi $(docker images 'org.kie.kogito/trusty-ui' -a -q)
docker rmi $(docker images 'org.kie.kogito/data-index-service-mongodb' -a -q)
docker rmi $(docker images 'org.kie.kogito/data-index-service-infinispan' -a -q)
docker rmi $(docker images 'org.kie.kogito/jobs-service-mongodb' -a -q)
docker rmi $(docker images 'org.kie.kogito/jobs-service-postgresql' -a -q)
docker rmi $(docker images 'org.kie.kogito/jobs-service-infinispan' -a -q)
docker rmi $(docker images 'org.kie.kogito/jobs-service-common' -a -q)
echo $KOGITO_APPS | tr -d '\r'
cd $(echo $KOGITO_APPS | tr -d '\r')
if [[ "$PWD" == */kogito-apps ]]
then 
    mvn clean install -DskipTests
else
    >&2 echo "error: script is not in kogito-apps ${KOGITO_APPS}"
    exit
fi

cd ui-packages/packages/trusty/it-tests/docker-compose
