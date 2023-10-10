import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  TaskInboxApi,
  TaskInboxChannelApi,
  TaskInboxEnvelopeApi,
  TaskInboxDriver,
  TaskInboxState
} from '../api';
import { TaskInboxChannelApiImpl } from './TaskInboxChannelApiImpl';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';

export interface Props {
  targetOrigin: string;
  initialState?: TaskInboxState;
  driver: TaskInboxDriver;
  allTaskStates?: string[];
  activeTaskStates?: string[];
}

export const EmbeddedTaskInbox = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<TaskInboxApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          TaskInboxChannelApi,
          TaskInboxEnvelopeApi
        >
      ): TaskInboxApi => ({
        taskInbox__notify: (userName) =>
          envelopeServer.envelopeApi.requests.taskInbox__notify(userName)
      }),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          TaskInboxChannelApi,
          TaskInboxEnvelopeApi
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
            postMessage(message, targetOrigin, transfer) {
              window.postMessage(message, targetOrigin, transfer);
            }
          }
        });
        return envelopeServer.envelopeApi.requests.taskInbox__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          {
            initialState: props.initialState,
            allTaskStates: props.allTaskStates,
            activeTaskStates: props.activeTaskStates
          }
        );
      },
      [props.allTaskStates, props.activeTaskStates]
    );

    return (
      <EmbeddedTaskInboxEnvelope
        ref={forwardedRef}
        apiImpl={new TaskInboxChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedTaskInboxEnvelope = React.forwardRef<
  TaskInboxApi,
  EmbeddedEnvelopeProps<TaskInboxChannelApi, TaskInboxEnvelopeApi, TaskInboxApi>
>(RefForwardingEmbeddedEnvelope);
