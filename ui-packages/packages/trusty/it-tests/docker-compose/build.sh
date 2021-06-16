#!/bin/bash
KOGITO_APPS=''
if [[ "$PWD" == */docker-compose ]]
then 
    KOGITO_APPS='../../../../..'
    echo 'script runs from the docker-compose folder'
elif [[ "$PWD" == */packages/trusty ]]
then 
    KOGITO_APPS='../../../'
    echo 'script runs from the trusty folder'
else
    >&2 echo "error: script starts from unexpected location: ${PWD}"
    >&2 echo "error: script expects /ui-packages/packages/trusty or /ui-packages/packages/trusty/it-tests/docker-compose folders"
    exit
fi

KIE_IMAGES=(
    'org.kie.kogito/integration-tests-trusty-service-quarkus'
    'org.kie.kogito/trusty-service-infinispan'
    'org.kie.kogito/explainability-service-messaging'
    'org.kie.kogito/trusty-ui'
    'org.kie.kogito/data-index-service-mongodb'
    'org.kie.kogito/data-index-service-infinispan'
    'org.kie.kogito/jobs-service-mongodb'
    'org.kie.kogito/jobs-service-postgresql'
    'org.kie.kogito/jobs-service-infinispan'
    'org.kie.kogito/jobs-service-common'
)

for IMG in ${KIE_IMAGES[@]}
    do
        HASH_IMG=$(docker images ${IMG} -a -q)
        if [ ! -z "$HASH_IMG" ]
        then
            echo "Remove image ${IMG}"
            docker rmi $HASH_IMG
        else
            echo "Image ${IMG} is not present"
        fi
    done

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
