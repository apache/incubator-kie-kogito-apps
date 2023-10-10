import React, { useCallback } from 'react';
import {
  TaskDetailsApi,
  TaskDetailsChannelApi,
  TaskDetailsEnvelopeApi
} from '../api';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import { EnvelopeBusMessage } from '@kie-tools-core/envelope-bus/dist/api';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { init } from '../envelope';

export type Props = {
  targetOrigin: string;
  userTask: UserTaskInstance;
};

export const EmbeddedTaskDetails = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<TaskDetailsApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          TaskDetailsChannelApi,
          TaskDetailsEnvelopeApi
        >
      ): TaskDetailsApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        // eslint-disable-next-line
        envelopeServer: EnvelopeServer<
          TaskDetailsChannelApi,
          TaskDetailsEnvelopeApi
        >,
        container: () => HTMLDivElement
      ) => {
        init({
          config: {
            containerType: ContainerType.DIV,
            envelopeId: envelopeServer.id
          },
          container: container(),
          bus: {
            postMessage<D, Type>(
              message: EnvelopeBusMessage<D, Type>,
              targetOrigin?: string,
              transfer?: any
            ) {
              window.parent.postMessage(message, targetOrigin, transfer);
            }
          }
        });
        return envelopeServer.envelopeApi.requests.taskDetails__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          {
            task: props.userTask
          }
        );
      },
      [props.userTask]
    );

    return (
      <EmbeddedTaskDetailsEnvelope
        ref={forwardedRef}
        apiImpl={props}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedTaskDetailsEnvelope = React.forwardRef<
  TaskDetailsApi,
  EmbeddedEnvelopeProps<
    TaskDetailsChannelApi,
    TaskDetailsEnvelopeApi,
    TaskDetailsApi
  >
>(RefForwardingEmbeddedEnvelope);
