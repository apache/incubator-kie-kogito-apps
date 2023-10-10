import { ApolloClient } from 'apollo-client';
import { SortBy, QueryFilter } from '@kogito-apps/task-inbox';
import { UserTaskInstance } from '@kogito-apps/task-console-shared';
import { GraphQL } from '@kogito-apps/consoles-common/dist/graphql';
import { User } from '@kogito-apps/consoles-common/dist/environment/auth';
import {
  buildTaskInboxWhereArgument,
  getOrderByObject
} from '../../utils/QueryUtils';

export interface TaskInboxQueries {
  getUserTaskById(taskId: string): Promise<UserTaskInstance>;

  getUserTasks(
    user: User,
    start: number,
    end: number,
    filters: QueryFilter,
    sortBy: SortBy
  ): Promise<UserTaskInstance[]>;
}

export class GraphQLTaskInboxQueries implements TaskInboxQueries {
  private readonly client: ApolloClient<any>;

  constructor(client: ApolloClient<any>) {
    this.client = client;
  }

  getUserTaskById(taskId: string): Promise<UserTaskInstance> {
    return new Promise<UserTaskInstance>((resolve, reject) => {
      this.client
        .query({
          query: GraphQL.GetUserTaskByIdDocument,
          variables: {
            id: taskId
          },
          fetchPolicy: 'network-only'
        })
        .then((value) => {
          if (
            value.data.UserTaskInstances &&
            value.data.UserTaskInstances.length > 0
          ) {
            resolve(value.data.UserTaskInstances[0]);
            return;
          }
          resolve(undefined);
        })
        .catch((reason) => reject(reason));
    });
  }

  getUserTasks(
    user: User,
    offset: number,
    limit: number,
    filters: QueryFilter,
    sortBy: SortBy
  ): Promise<UserTaskInstance[]> {
    return new Promise<UserTaskInstance[]>((resolve, reject) => {
      this.client
        .query({
          query: GraphQL.GetTasksForUserDocument,
          variables: {
            whereArgument: buildTaskInboxWhereArgument(user, filters),
            offset: offset,
            limit: limit,
            orderBy: getOrderByObject(sortBy)
          },
          fetchPolicy: 'network-only'
        })
        .then((value) => {
          resolve(value.data.UserTaskInstances);
        })
        .catch((reason) => reject(reason));
    });
  }
}
