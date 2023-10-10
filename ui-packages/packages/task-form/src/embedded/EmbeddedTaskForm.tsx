import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import {
  TaskFormApi,
  TaskFormChannelApi,
  TaskFormDriver,
  TaskFormEnvelopeApi,
  User
} from '../api';
import { EmbeddedTaskFormChannelApiImpl } from './EmbeddedTaskFormChannelApiImpl';
import { init } from '../envelope';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';

export interface EmbeddedTaskFormProps {
  targetOrigin: string;
  userTask: UserTaskInstance;
  driver: TaskFormDriver;
  user: User;
}

export const EmbeddedTaskForm = React.forwardRef(
  (props: EmbeddedTaskFormProps, forwardedRef: React.Ref<TaskFormApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<TaskFormChannelApi, TaskFormEnvelopeApi>
      ): TaskFormApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<TaskFormChannelApi, TaskFormEnvelopeApi>,
        container: () => HTMLDivElement
      ) => {
        init({
          config: {
            containerType: ContainerType.DIV,
            envelopeId: envelopeServer.id
          },
          container: container(),
          bus: {
            postMessage(message, targetOrigin, transfer) {
              window.postMessage(message, targetOrigin, transfer);
            }
          },
          targetOrigin: props.targetOrigin
        });
        return envelopeServer.envelopeApi.requests.taskForm__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          { userTask: props.userTask, user: props.user }
        );
      },
      []
    );

    return (
      <EmbeddedTaskFormEnvelope
        ref={forwardedRef}
        apiImpl={new EmbeddedTaskFormChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedTaskFormEnvelope = React.forwardRef<
  TaskFormApi,
  EmbeddedEnvelopeProps<TaskFormChannelApi, TaskFormEnvelopeApi, TaskFormApi>
>(RefForwardingEmbeddedEnvelope);
