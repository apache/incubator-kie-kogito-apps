import React, { useContext, useEffect, useState } from 'react';

import { isEmpty } from 'lodash';
import axios from 'axios';
import {
  KogitoEmptyState,
  KogitoEmptyStateType,
  KogitoSpinner
} from '@kogito-apps/common';
import { TaskInfo } from '../../../model/TaskInfo';
import { FormSchema } from '../../../model/FormSchema';
import TaskConsoleContext, {
  IContext
} from '../../../context/TaskConsoleContext/TaskConsoleContext';
import FormNotification from '../../Atoms/FormNotification/FormNotification';
import FormRenderer from '../../Molecules/FormRenderer/FormRenderer';

interface IOwnProps {
  taskInfo?: TaskInfo;
  successCallback?: () => void;
  errorCallback?: () => void;
}

interface AlertMessage {
  message: string;
  callback: () => void;
}

const TaskForm: React.FC<IOwnProps> = ({
  taskInfo,
  successCallback,
  errorCallback
}) => {
  // tslint:disable: no-floating-promises
  const context: IContext<TaskInfo> = useContext(TaskConsoleContext);
  const [alertMessage, setAlertMessage]: [AlertMessage, any] = useState(null);
  const [loading, setLoading] = useState(true);
  const [userTaskInfo, setUserTaskInfo]: [TaskInfo, any] = useState(null);
  const [taskFormSchema, setTaskFormSchema]: [FormSchema, any] = useState(null);

  if (!userTaskInfo) {
    if (taskInfo) {
      setUserTaskInfo(taskInfo);
    } else {
      if (context.getActiveItem()) {
        setUserTaskInfo(context.getActiveItem());
      }
    }
  }

  useEffect(() => {
    loadForm();
  }, [userTaskInfo]);

  const loadForm = () => {
    if (userTaskInfo) {
      axios
        .get(userTaskInfo.getTaskEndPoint() + '/schema', {
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
            crossorigin: 'true',
            'Access-Control-Allow-Origin': '*'
          }
        })
        .then(res => {
          if (res.status === 200) {
            setTaskFormSchema(res.data);
          }
          setLoading(false);
        })
        .catch(er => {
          setLoading(false);
        });
    }
  };

  const showAlertMessage = (submitMessage: string, submitCallback?: any) => {
    setAlertMessage({
      message: submitMessage,
      callback: () => {
        setAlertMessage(null);
        if (submitCallback) {
          submitCallback();
        }
      }
    });
  };

  if (userTaskInfo) {
    if (loading) {
      return (
        <KogitoSpinner
          spinnerText={'Loading form for task: ' + userTaskInfo.task.name}
        />
      );
    }

    if (taskFormSchema) {
      const outputs = JSON.parse(userTaskInfo.task.outputs);
      const formData = !isEmpty(outputs)
        ? outputs
        : JSON.parse(userTaskInfo.task.inputs);

      return (
        <React.Fragment>
          {alertMessage && (
            <FormNotification
              message={alertMessage.message}
              closeAction={alertMessage.callback}
            />
          )}
          <FormRenderer
            taskInfo={taskInfo}
            formSchema={taskFormSchema}
            model={formData}
            successCallback={phase => {
              const message = `Task '${taskInfo.task.id}' successfully transitioned to phase '${phase}'.`;
              showAlertMessage(message, successCallback);
            }}
            errorCallback={(phase, error) => {
              let message = `Task '${taskInfo.task.id}' couldn't transition to phase '${phase}'.`;

              if (error) {
                message += ` Error: '${error}'`;
              }
              showAlertMessage(message, errorCallback);
            }}
          />
        </React.Fragment>
      );
    }
  }

  return (
    <KogitoEmptyState
      type={KogitoEmptyStateType.Info}
      title="No form to show"
      body="Cannot find form"
    />
  );
};

export default TaskForm;
