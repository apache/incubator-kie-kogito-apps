import React, { useCallback } from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { useCloudEventFormGatewayApi } from '../../../channel/CloudEventForm';
import { EmbeddedCloudEventForm } from '@kogito-apps/cloud-event-form/dist/embedded';
import { CloudEventRequest } from '@kogito-apps/cloud-event-form/dist/api';
import { useParams } from 'react-router';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

export type CloudEventFormContainerProps = {
  isTriggerNewInstance: boolean;
  onSuccess: (id: string) => void;
  onError: (details?: string) => void;
};

export type CloudEventFormContainerParams = {
  instanceId?: string;
};

const CloudEventFormContainer: React.FC<
  CloudEventFormContainerProps & OUIAProps
> = ({ isTriggerNewInstance, onSuccess, onError, ouiaId, ouiaSafe }) => {
  const gatewayApi = useCloudEventFormGatewayApi();
  const appContext = useDevUIAppContext();

  const { instanceId } = useParams<CloudEventFormContainerParams>();

  const triggerStartCloudEvent = useCallback(
    (event: CloudEventRequest) => {
      return gatewayApi
        .triggerStartCloudEvent(event)
        .then((businessKey) => {
          onSuccess(
            `A workflow with business key ${businessKey} has been successfully triggered.`
          );
        })
        .catch((error) => handleError(error));
    },
    [gatewayApi, onSuccess, onError]
  );

  const triggerCloudEvent = useCallback(
    (event: CloudEventRequest) => {
      return gatewayApi
        .triggerCloudEvent(event)
        .then((response) => {
          console.log(response);
          onSuccess('The CloudEvent has been successfully triggered.');
        })
        .catch((error) => handleError(error));
    },
    [gatewayApi, onSuccess, onError]
  );

  const handleError = useCallback(
    (error) => {
      const message =
        error?.message ||
        'Unknown error. More details in the developer tools console.';
      onError(message);
    },
    [gatewayApi, onSuccess, onError]
  );

  return (
    <EmbeddedCloudEventForm
      {...componentOuiaProps(ouiaId, 'cloud-event-form-container', ouiaSafe)}
      targetOrigin={appContext.getDevUIUrl()}
      isNewInstanceEvent={isTriggerNewInstance}
      defaultValues={{
        cloudEventSource: '/local/quarkus-devUi',
        instanceId: instanceId ?? undefined
      }}
      driver={{
        triggerCloudEvent(event: CloudEventRequest): Promise<void> {
          const doTrigger = isTriggerNewInstance
            ? triggerStartCloudEvent
            : triggerCloudEvent;
          return doTrigger(event);
        }
      }}
    />
  );
};

export default CloudEventFormContainer;
