import React, { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import _ from 'lodash';
import { TaskInboxChannelApi, TaskInboxState } from '../api';
import TaskInbox from './components/TaskInbox/TaskInbox';
import TaskInboxEnvelopeViewDriver from './TaskInboxEnvelopeViewDriver';
import '@patternfly/patternfly/patternfly.css';
import {
  getDefaultActiveTaskStates,
  getDefaultTaskStates
} from './components/utils/TaskInboxUtils';

export interface TaskInboxEnvelopeViewApi {
  initialize: (
    initialState?: TaskInboxState,
    allTaskStates?: string[],
    activeTaskStates?: string[],
    userName?: string
  ) => void;
  notify: (userName) => Promise<void>;
}

interface Props {
  channelApi: MessageBusClientApi<TaskInboxChannelApi>;
}

export const TaskInboxEnvelopeView = React.forwardRef<
  TaskInboxEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  const [isEnvelopeConnectedToChannel, setEnvelopeConnectedToChannel] =
    useState<boolean>(false);
  const [initialState, setInitialState] = useState<TaskInboxState>();
  const [allTaskStates, setAllTaskStates] = useState<string[]>(
    getDefaultTaskStates()
  );
  const [activeTaskStates, setActiveTaskStates] = useState<string[]>(
    getDefaultActiveTaskStates()
  );
  const [currentUser, setCurrentUser] = useState<string>('');
  useImperativeHandle(
    forwardedRef,
    () => ({
      initialize: (_initialState?, _allTaskStates?, _activeTaskStates?) => {
        setInitialState(_initialState);
        if (!_.isEmpty(_allTaskStates)) {
          setAllTaskStates(_allTaskStates);
        }
        if (!_.isEmpty(_activeTaskStates)) {
          setActiveTaskStates(_activeTaskStates);
        }

        setEnvelopeConnectedToChannel(true);
      },
      notify: (userName) => {
        if (!_.isEmpty(userName)) {
          setCurrentUser(userName);
        }
        return Promise.resolve();
      }
    }),
    []
  );

  return (
    <React.Fragment>
      <TaskInbox
        isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
        driver={new TaskInboxEnvelopeViewDriver(props.channelApi)}
        initialState={initialState}
        allTaskStates={allTaskStates}
        activeTaskStates={activeTaskStates}
        currentUser={currentUser}
      />
    </React.Fragment>
  );
});

export default TaskInboxEnvelopeView;
