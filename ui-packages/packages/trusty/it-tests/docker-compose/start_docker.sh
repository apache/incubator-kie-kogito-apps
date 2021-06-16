#!/bin/bash
KOGITO_APPS=''
if [[ "$PWD" == */docker-compose ]]
then 
    echo 'Application starts from the docker-compose folder - probably manual start of this script'
elif [[ "$PWD" == */packages/trusty ]]
then 
    echo 'Application starts from the trusty folder - probably run some yarn script'
    echo 'Move to the docker-compose folder'
    cd it-tests/docker-compose
else
    >&2 echo "error: script starts from unexpected location: ${PWD}"
    >&2 echo "error: script expects /ui-packages/packages/trusty or /ui-packages/packages/trusty/it-tests/docker-compose folders"
    exit
fi

docker-compose up

