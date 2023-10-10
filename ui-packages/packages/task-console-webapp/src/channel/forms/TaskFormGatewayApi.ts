import axios from 'axios';
import { User } from '@kogito-apps/consoles-common/dist/environment/auth';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { CustomForm } from '@kogito-apps/task-form';

export interface TaskFormGatewayApi {
  getTaskFormSchema(userTask: UserTaskInstance): Promise<Record<string, any>>;

  getCustomForm(userTask: UserTaskInstance): Promise<CustomForm>;

  doSubmit(
    userTask: UserTaskInstance,
    phase: string,
    payload: any
  ): Promise<any>;
}

export class TaskFormGatewayApiImpl implements TaskFormGatewayApi {
  constructor(private readonly user: User) {}

  doSubmit(
    userTask: UserTaskInstance,
    phase: string,
    payload: any
  ): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const endpoint = `${
        userTask.endpoint
      }?phase=${phase}&${getTaskEndpointSecurityParams(this.user)}`;
      axios
        .post(endpoint, payload, {
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json'
          }
        })
        .then((response) => {
          if (response.status == 200) {
            resolve(response.data);
          } else {
            reject(response);
          }
        })
        .catch((error) => reject(error));
    });
  }

  getTaskFormSchema(userTask: UserTaskInstance): Promise<Record<string, any>> {
    return new Promise<Record<string, any>>((resolve, reject) => {
      const endpoint = getTaskSchemaEndPoint(userTask, this.user);
      axios
        .get(endpoint, {
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json'
          }
        })
        .then((response) => {
          if (response.status == 200) {
            resolve(response.data);
          } else {
            reject(response);
          }
        })
        .catch((error) => reject(error));
    });
  }

  getCustomForm(userTask: UserTaskInstance): Promise<CustomForm> {
    return Promise.reject();
  }
}

function getTaskSchemaEndPoint(task: UserTaskInstance, user: User): string {
  let params = '';
  let endpoint = task.endpoint;

  if (task.completed) {
    // if task is completed we load the schema for the task definition
    endpoint = endpoint.slice(0, -(task.id.length + 1));
    endpoint = endpoint.replace(task.processInstanceId + '/', '');
  } else {
    params = `?${getTaskEndpointSecurityParams(user)}`;
  }

  return `${endpoint}/schema${params}`;
}

function getTaskEndpointSecurityParams(user: User): string {
  let groups = '';

  if (user.groups && user.groups.length > 0) {
    groups = `&group=${user.groups.join('&group=')}`;
  }
  return `user=${user.id}${groups}`;
}
