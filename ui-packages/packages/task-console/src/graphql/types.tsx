/* tslint:disable */
import gql from 'graphql-tag';
import * as ApolloReactCommon from '@apollo/react-common';
import * as ApolloReactHooks from '@apollo/react-hooks';
export type Maybe<T> = T | null;

/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string,
  String: string,
  Boolean: boolean,
  Int: number,
  Float: number,
  DateTime: any,
};

export type BooleanArgument = {
  isNull?: Maybe<Scalars['Boolean']>,
  equal?: Maybe<Scalars['Boolean']>,
};

export type DateArgument = {
  isNull?: Maybe<Scalars['Boolean']>,
  equal?: Maybe<Scalars['DateTime']>,
  greaterThan?: Maybe<Scalars['DateTime']>,
  greaterThanEqual?: Maybe<Scalars['DateTime']>,
  lessThan?: Maybe<Scalars['DateTime']>,
  lessThanEqual?: Maybe<Scalars['DateTime']>,
  between?: Maybe<DateRange>,
};

export type DateRange = {
  from: Scalars['DateTime'],
  to: Scalars['DateTime'],
};

export type IdArgument = {
  in?: Maybe<Array<Scalars['String']>>,
  equal?: Maybe<Scalars['String']>,
  isNull?: Maybe<Scalars['Boolean']>,
};

export type Job = {
   __typename?: 'Job',
  id: Scalars['String'],
  processId?: Maybe<Scalars['String']>,
  processInstanceId?: Maybe<Scalars['String']>,
  rootProcessInstanceId?: Maybe<Scalars['String']>,
  rootProcessId?: Maybe<Scalars['String']>,
  status: JobStatus,
  expirationTime?: Maybe<Scalars['DateTime']>,
  priority?: Maybe<Scalars['Int']>,
  callbackEndpoint?: Maybe<Scalars['String']>,
  repeatInterval?: Maybe<Scalars['Int']>,
  repeatLimit?: Maybe<Scalars['Int']>,
  scheduledId?: Maybe<Scalars['String']>,
  retries?: Maybe<Scalars['Int']>,
  lastUpdate?: Maybe<Scalars['DateTime']>,
  executionCounter?: Maybe<Scalars['Int']>,
};

export type JobArgument = {
  and?: Maybe<Array<JobArgument>>,
  or?: Maybe<Array<JobArgument>>,
  id?: Maybe<IdArgument>,
  processId?: Maybe<StringArgument>,
  processInstanceId?: Maybe<IdArgument>,
  rootProcessInstanceId?: Maybe<IdArgument>,
  rootProcessId?: Maybe<StringArgument>,
  status?: Maybe<JobStatusArgument>,
  expirationTime?: Maybe<DateArgument>,
  priority?: Maybe<NumericArgument>,
  scheduledId?: Maybe<IdArgument>,
  lastUpdate?: Maybe<DateArgument>,
};

export type JobOrderBy = {
  processId?: Maybe<OrderBy>,
  rootProcessId?: Maybe<OrderBy>,
  status?: Maybe<OrderBy>,
  expirationTime?: Maybe<OrderBy>,
  priority?: Maybe<OrderBy>,
  retries?: Maybe<OrderBy>,
  lastUpdate?: Maybe<OrderBy>,
  executionCounter?: Maybe<OrderBy>,
};

export enum JobStatus {
  Error = 'ERROR',
  Executed = 'EXECUTED',
  Scheduled = 'SCHEDULED',
  Retry = 'RETRY',
  Canceled = 'CANCELED'
}

export type JobStatusArgument = {
  equal?: Maybe<JobStatus>,
  in?: Maybe<Array<Maybe<JobStatus>>>,
};

export type KogitoMetadata = {
   __typename?: 'KogitoMetadata',
  lastUpdate: Scalars['DateTime'],
  processInstances?: Maybe<Array<Maybe<ProcessInstanceMeta>>>,
  userTasks?: Maybe<Array<Maybe<UserTaskInstanceMeta>>>,
};

export type KogitoMetadataArgument = {
  lastUpdate?: Maybe<DateArgument>,
  processInstances?: Maybe<ProcessInstanceMetaArgument>,
  userTasks?: Maybe<UserTaskInstanceMetaArgument>,
};

export type KogitoMetadataOrderBy = {
  lastUpdate?: Maybe<OrderBy>,
};

export type Milestone = {
   __typename?: 'Milestone',
  id: Scalars['String'],
  name: Scalars['String'],
  status: MilestoneStatus,
};

export type MilestoneArgument = {
  id?: Maybe<IdArgument>,
  name?: Maybe<StringArgument>,
  status?: Maybe<MilestoneStatusArgument>,
};

export enum MilestoneStatus {
  Available = 'AVAILABLE',
  Active = 'ACTIVE',
  Completed = 'COMPLETED'
}

export type MilestoneStatusArgument = {
  equal?: Maybe<MilestoneStatus>,
  in?: Maybe<Array<Maybe<MilestoneStatus>>>,
};

export type NodeInstance = {
   __typename?: 'NodeInstance',
  id: Scalars['String'],
  name: Scalars['String'],
  type: Scalars['String'],
  enter: Scalars['DateTime'],
  exit?: Maybe<Scalars['DateTime']>,
  definitionId: Scalars['String'],
  nodeId: Scalars['String'],
};

export type NodeInstanceArgument = {
  id?: Maybe<IdArgument>,
  name?: Maybe<StringArgument>,
  definitionId?: Maybe<StringArgument>,
  nodeId?: Maybe<StringArgument>,
  type?: Maybe<StringArgument>,
  enter?: Maybe<DateArgument>,
  exit?: Maybe<DateArgument>,
};

export type NumericArgument = {
  in?: Maybe<Array<Scalars['Int']>>,
  isNull?: Maybe<Scalars['Boolean']>,
  equal?: Maybe<Scalars['Int']>,
  greaterThan?: Maybe<Scalars['Int']>,
  greaterThanEqual?: Maybe<Scalars['Int']>,
  lessThan?: Maybe<Scalars['Int']>,
  lessThanEqual?: Maybe<Scalars['Int']>,
  between?: Maybe<NumericRange>,
};

export type NumericRange = {
  from: Scalars['Int'],
  to: Scalars['Int'],
};

export enum OrderBy {
  Asc = 'ASC',
  Desc = 'DESC'
}

export type Pagination = {
  limit?: Maybe<Scalars['Int']>,
  offset?: Maybe<Scalars['Int']>,
};

export type ProcessInstance = {
   __typename?: 'ProcessInstance',
  id: Scalars['String'],
  processId: Scalars['String'],
  processName?: Maybe<Scalars['String']>,
  parentProcessInstanceId?: Maybe<Scalars['String']>,
  rootProcessInstanceId?: Maybe<Scalars['String']>,
  rootProcessId?: Maybe<Scalars['String']>,
  roles?: Maybe<Array<Scalars['String']>>,
  state: ProcessInstanceState,
  endpoint: Scalars['String'],
  serviceUrl?: Maybe<Scalars['String']>,
  nodes: Array<NodeInstance>,
  milestones?: Maybe<Array<Milestone>>,
  variables?: Maybe<Scalars['String']>,
  start: Scalars['DateTime'],
  end?: Maybe<Scalars['DateTime']>,
  parentProcessInstance?: Maybe<ProcessInstance>,
  childProcessInstances?: Maybe<Array<ProcessInstance>>,
  error?: Maybe<ProcessInstanceError>,
  addons?: Maybe<Array<Scalars['String']>>,
  lastUpdate: Scalars['DateTime'],
  businessKey?: Maybe<Scalars['String']>,
};

export type ProcessInstanceArgument = {
  and?: Maybe<Array<ProcessInstanceArgument>>,
  or?: Maybe<Array<ProcessInstanceArgument>>,
  id?: Maybe<IdArgument>,
  processId?: Maybe<StringArgument>,
  processName?: Maybe<StringArgument>,
  parentProcessInstanceId?: Maybe<IdArgument>,
  rootProcessInstanceId?: Maybe<IdArgument>,
  rootProcessId?: Maybe<StringArgument>,
  state?: Maybe<ProcessInstanceStateArgument>,
  error?: Maybe<ProcessInstanceErrorArgument>,
  nodes?: Maybe<NodeInstanceArgument>,
  milestones?: Maybe<MilestoneArgument>,
  endpoint?: Maybe<StringArgument>,
  roles?: Maybe<StringArrayArgument>,
  start?: Maybe<DateArgument>,
  end?: Maybe<DateArgument>,
  addons?: Maybe<StringArrayArgument>,
  lastUpdate?: Maybe<DateArgument>,
  businessKey?: Maybe<StringArgument>,
};

export type ProcessInstanceError = {
   __typename?: 'ProcessInstanceError',
  nodeDefinitionId: Scalars['String'],
  message?: Maybe<Scalars['String']>,
};

export type ProcessInstanceErrorArgument = {
  nodeDefinitionId?: Maybe<StringArgument>,
  message?: Maybe<StringArgument>,
};

export type ProcessInstanceErrorOrderBy = {
  nodeDefinitionId?: Maybe<OrderBy>,
  message?: Maybe<OrderBy>,
};

export type ProcessInstanceMeta = {
   __typename?: 'ProcessInstanceMeta',
  id: Scalars['String'],
  processId: Scalars['String'],
  processName?: Maybe<Scalars['String']>,
  parentProcessInstanceId?: Maybe<Scalars['String']>,
  rootProcessInstanceId?: Maybe<Scalars['String']>,
  rootProcessId?: Maybe<Scalars['String']>,
  roles?: Maybe<Array<Scalars['String']>>,
  state: ProcessInstanceState,
  endpoint: Scalars['String'],
  serviceUrl?: Maybe<Scalars['String']>,
  start: Scalars['DateTime'],
  end?: Maybe<Scalars['DateTime']>,
  lastUpdate: Scalars['DateTime'],
  businessKey?: Maybe<Scalars['String']>,
};

export type ProcessInstanceMetaArgument = {
  id?: Maybe<IdArgument>,
  processId?: Maybe<StringArgument>,
  processName?: Maybe<StringArgument>,
  parentProcessInstanceId?: Maybe<IdArgument>,
  rootProcessInstanceId?: Maybe<IdArgument>,
  rootProcessId?: Maybe<StringArgument>,
  state?: Maybe<ProcessInstanceStateArgument>,
  endpoint?: Maybe<StringArgument>,
  roles?: Maybe<StringArrayArgument>,
  start?: Maybe<DateArgument>,
  end?: Maybe<DateArgument>,
  businessKey?: Maybe<StringArgument>,
};

export type ProcessInstanceOrderBy = {
  processId?: Maybe<OrderBy>,
  processName?: Maybe<OrderBy>,
  rootProcessId?: Maybe<OrderBy>,
  state?: Maybe<OrderBy>,
  start?: Maybe<OrderBy>,
  end?: Maybe<OrderBy>,
  error?: Maybe<ProcessInstanceErrorOrderBy>,
  lastUpdate?: Maybe<OrderBy>,
  businessKey?: Maybe<OrderBy>,
};

export enum ProcessInstanceState {
  Pending = 'PENDING',
  Active = 'ACTIVE',
  Completed = 'COMPLETED',
  Aborted = 'ABORTED',
  Suspended = 'SUSPENDED',
  Error = 'ERROR'
}

export type ProcessInstanceStateArgument = {
  equal?: Maybe<ProcessInstanceState>,
  in?: Maybe<Array<Maybe<ProcessInstanceState>>>,
};

export type Query = {
   __typename?: 'Query',
  ProcessInstances?: Maybe<Array<Maybe<ProcessInstance>>>,
  UserTaskInstances?: Maybe<Array<Maybe<UserTaskInstance>>>,
  Jobs?: Maybe<Array<Maybe<Job>>>,
};

export type QueryProcessInstancesArgs = {
  where?: Maybe<ProcessInstanceArgument>,
  orderBy?: Maybe<ProcessInstanceOrderBy>,
  pagination?: Maybe<Pagination>
};

export type QueryUserTaskInstancesArgs = {
  where?: Maybe<UserTaskInstanceArgument>,
  orderBy?: Maybe<UserTaskInstanceOrderBy>,
  pagination?: Maybe<Pagination>
};

export type QueryJobsArgs = {
  where?: Maybe<JobArgument>,
  orderBy?: Maybe<JobOrderBy>,
  pagination?: Maybe<Pagination>
};

export type StringArgument = {
  in?: Maybe<Array<Scalars['String']>>,
  like?: Maybe<Scalars['String']>,
  isNull?: Maybe<Scalars['Boolean']>,
  equal?: Maybe<Scalars['String']>,
};

export type StringArrayArgument = {
  contains?: Maybe<Scalars['String']>,
  containsAll?: Maybe<Array<Scalars['String']>>,
  containsAny?: Maybe<Array<Scalars['String']>>,
  isNull?: Maybe<Scalars['Boolean']>,
};

export type Subscription = {
   __typename?: 'Subscription',
  ProcessInstanceAdded: ProcessInstance,
  ProcessInstanceUpdated: ProcessInstance,
  UserTaskInstanceAdded: UserTaskInstance,
  UserTaskInstanceUpdated: UserTaskInstance,
  JobAdded: Job,
  JobUpdated: Job,
};

export type UserTaskInstance = {
   __typename?: 'UserTaskInstance',
  id: Scalars['String'],
  description?: Maybe<Scalars['String']>,
  name?: Maybe<Scalars['String']>,
  priority?: Maybe<Scalars['String']>,
  processInstanceId: Scalars['String'],
  processId: Scalars['String'],
  rootProcessInstanceId?: Maybe<Scalars['String']>,
  rootProcessId?: Maybe<Scalars['String']>,
  state: Scalars['String'],
  actualOwner?: Maybe<Scalars['String']>,
  adminGroups?: Maybe<Array<Scalars['String']>>,
  adminUsers?: Maybe<Array<Scalars['String']>>,
  completed?: Maybe<Scalars['DateTime']>,
  started: Scalars['DateTime'],
  excludedUsers?: Maybe<Array<Scalars['String']>>,
  potentialGroups?: Maybe<Array<Scalars['String']>>,
  potentialUsers?: Maybe<Array<Scalars['String']>>,
  inputs?: Maybe<Scalars['String']>,
  outputs?: Maybe<Scalars['String']>,
  referenceName?: Maybe<Scalars['String']>,
  lastUpdate: Scalars['DateTime'],
};

export type UserTaskInstanceArgument = {
  and?: Maybe<Array<UserTaskInstanceArgument>>,
  or?: Maybe<Array<UserTaskInstanceArgument>>,
  state?: Maybe<StringArgument>,
  id?: Maybe<IdArgument>,
  description?: Maybe<StringArgument>,
  name?: Maybe<StringArgument>,
  priority?: Maybe<StringArgument>,
  processInstanceId?: Maybe<IdArgument>,
  actualOwner?: Maybe<StringArgument>,
  potentialUsers?: Maybe<StringArrayArgument>,
  potentialGroups?: Maybe<StringArrayArgument>,
  excludedUsers?: Maybe<StringArrayArgument>,
  adminGroups?: Maybe<StringArrayArgument>,
  adminUsers?: Maybe<StringArrayArgument>,
  completed?: Maybe<DateArgument>,
  started?: Maybe<DateArgument>,
  referenceName?: Maybe<StringArgument>,
  lastUpdate?: Maybe<DateArgument>,
};

export type UserTaskInstanceMeta = {
   __typename?: 'UserTaskInstanceMeta',
  id: Scalars['String'],
  description?: Maybe<Scalars['String']>,
  name?: Maybe<Scalars['String']>,
  priority?: Maybe<Scalars['String']>,
  processInstanceId: Scalars['String'],
  state: Scalars['String'],
  actualOwner?: Maybe<Scalars['String']>,
  adminGroups?: Maybe<Array<Scalars['String']>>,
  adminUsers?: Maybe<Array<Scalars['String']>>,
  completed?: Maybe<Scalars['DateTime']>,
  started: Scalars['DateTime'],
  excludedUsers?: Maybe<Array<Scalars['String']>>,
  potentialGroups?: Maybe<Array<Scalars['String']>>,
  potentialUsers?: Maybe<Array<Scalars['String']>>,
  referenceName?: Maybe<Scalars['String']>,
  lastUpdate: Scalars['DateTime'],
};

export type UserTaskInstanceMetaArgument = {
  state?: Maybe<StringArgument>,
  id?: Maybe<IdArgument>,
  description?: Maybe<StringArgument>,
  name?: Maybe<StringArgument>,
  priority?: Maybe<StringArgument>,
  processInstanceId?: Maybe<IdArgument>,
  actualOwner?: Maybe<StringArgument>,
  potentialUsers?: Maybe<StringArrayArgument>,
  potentialGroups?: Maybe<StringArrayArgument>,
  excludedUsers?: Maybe<StringArrayArgument>,
  adminGroups?: Maybe<StringArrayArgument>,
  adminUsers?: Maybe<StringArrayArgument>,
  completed?: Maybe<DateArgument>,
  started?: Maybe<DateArgument>,
  referenceName?: Maybe<StringArgument>,
};

export type UserTaskInstanceOrderBy = {
  state?: Maybe<OrderBy>,
  actualOwner?: Maybe<OrderBy>,
  description?: Maybe<OrderBy>,
  name?: Maybe<OrderBy>,
  priority?: Maybe<OrderBy>,
  completed?: Maybe<OrderBy>,
  started?: Maybe<OrderBy>,
  referenceName?: Maybe<OrderBy>,
  lastUpdate?: Maybe<OrderBy>,
};

export type GetUserTasksByStateQueryVariables = {
  state?: Maybe<Scalars['String']>
};

export type GetUserTasksByStateQuery = (
  { __typename?: 'Query' }
  & { UserTaskInstances: Maybe<Array<Maybe<(
    { __typename?: 'UserTaskInstance' }
    & Pick<UserTaskInstance, 'id' | 'description' | 'name' | 'priority' | 'processInstanceId' | 'processId' | 'rootProcessInstanceId' | 'rootProcessId' | 'state' | 'actualOwner' | 'adminGroups' | 'adminUsers' | 'completed' | 'started' | 'excludedUsers' | 'potentialGroups' | 'potentialUsers' | 'inputs' | 'outputs' | 'referenceName'>
  )>>> }
);

export type GetUserTasksByStatesQueryVariables = {
  state?: Maybe<Array<Scalars['String']>>
};

export type GetUserTasksByStatesQuery = (
  { __typename?: 'Query' }
  & { UserTaskInstances: Maybe<Array<Maybe<(
    { __typename?: 'UserTaskInstance' }
    & Pick<UserTaskInstance, 'id' | 'description' | 'name' | 'priority' | 'processInstanceId' | 'processId' | 'rootProcessInstanceId' | 'rootProcessId' | 'state' | 'actualOwner' | 'adminGroups' | 'adminUsers' | 'completed' | 'started' | 'excludedUsers' | 'potentialGroups' | 'potentialUsers' | 'inputs' | 'outputs' | 'referenceName'>
  )>>> }
);

export type GetUserTaskByIdQueryVariables = {
  id?: Maybe<Scalars['String']>
};


export type GetUserTaskByIdQuery = (
  { __typename?: 'Query' }
  & { UserTaskInstances: Maybe<Array<Maybe<(
    { __typename?: 'UserTaskInstance' }
    & Pick<UserTaskInstance, 'id' | 'description' | 'name' | 'priority' | 'processInstanceId' | 'processId' | 'rootProcessInstanceId' | 'rootProcessId' | 'state' | 'actualOwner' | 'adminGroups' | 'adminUsers' | 'completed' | 'started' | 'excludedUsers' | 'potentialGroups' | 'potentialUsers' | 'inputs' | 'outputs' | 'referenceName'>
  )>>> }
);

export type GetProcessInstanceByIdQueryVariables = {
  id?: Maybe<Scalars['String']>
};


export type GetProcessInstanceByIdQuery = (
  { __typename?: 'Query' }
  & { ProcessInstances: Maybe<Array<Maybe<(
    { __typename?: 'ProcessInstance' }
    & Pick<ProcessInstance, 'id' | 'processId' | 'processName' | 'endpoint'>
  )>>> }
);

export type GetTaskForUserQueryVariables = {
  user?: Maybe<Scalars['String']>,
  groups?: Maybe<Array<Scalars['String']>>,
  offset?: Maybe<Scalars['Int']>,
  limit?: Maybe<Scalars['Int']>
};


export type GetTaskForUserQuery = (
  { __typename?: 'Query' }
  & { UserTaskInstances: Maybe<Array<Maybe<(
    { __typename?: 'UserTaskInstance' }
    & Pick<UserTaskInstance, 'id' | 'name' | 'referenceName' | 'description' | 'priority' | 'processInstanceId' | 'processId' | 'rootProcessInstanceId' | 'rootProcessId' | 'state' | 'actualOwner' | 'adminGroups' | 'adminUsers' | 'completed' | 'started' | 'excludedUsers' | 'potentialGroups' | 'potentialUsers' | 'inputs' | 'outputs' | 'lastUpdate'>
  )>>> }
);


export const GetUserTasksByStateDocument = gql`
    query getUserTasksByState($state: String) {
  UserTaskInstances(where: {state: {equal: $state}}) {
    id
    description
    name
    priority
    processInstanceId
    processId
    rootProcessInstanceId
    rootProcessId
    state
    actualOwner
    adminGroups
    adminUsers
    completed
    started
    excludedUsers
    potentialGroups
    potentialUsers
    inputs
    outputs
    referenceName
  }
}
    `;

/**
 * __useGetUserTasksByStateQuery__
 *
 * To run a query within a React component, call `useGetUserTasksByStateQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetUserTasksByStateQuery` returns an object from Apollo Client that contains loading, error, and data properties 
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetUserTasksByStateQuery({
 *   variables: {
 *      state: // value for 'state'
 *   },
 * });
 */
export function useGetUserTasksByStateQuery(baseOptions?: ApolloReactHooks.QueryHookOptions<GetUserTasksByStateQuery, GetUserTasksByStateQueryVariables>) {
        return ApolloReactHooks.useQuery<GetUserTasksByStateQuery, GetUserTasksByStateQueryVariables>(GetUserTasksByStateDocument, baseOptions);
      }
export function useGetUserTasksByStateLazyQuery(baseOptions?: ApolloReactHooks.LazyQueryHookOptions<GetUserTasksByStateQuery, GetUserTasksByStateQueryVariables>) {
          return ApolloReactHooks.useLazyQuery<GetUserTasksByStateQuery, GetUserTasksByStateQueryVariables>(GetUserTasksByStateDocument, baseOptions);
        }
export type GetUserTasksByStateQueryHookResult = ReturnType<typeof useGetUserTasksByStateQuery>;
export type GetUserTasksByStateLazyQueryHookResult = ReturnType<typeof useGetUserTasksByStateLazyQuery>;
export type GetUserTasksByStateQueryResult = ApolloReactCommon.QueryResult<GetUserTasksByStateQuery, GetUserTasksByStateQueryVariables>;
export const GetUserTasksByStatesDocument = gql`
    query getUserTasksByStates($state: [String!]) {
  UserTaskInstances(where: {state: {in: $state}}) {
    id
    description
    name
    priority
    processInstanceId
    processId
    rootProcessInstanceId
    rootProcessId
    state
    actualOwner
    adminGroups
    adminUsers
    completed
    started
    excludedUsers
    potentialGroups
    potentialUsers
    inputs
    outputs
    referenceName
  }
}
    `;

/**
 * __useGetUserTasksByStatesQuery__
 *
 * To run a query within a React component, call `useGetUserTasksByStatesQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetUserTasksByStatesQuery` returns an object from Apollo Client that contains loading, error, and data properties 
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetUserTasksByStatesQuery({
 *   variables: {
 *      state: // value for 'state'
 *   },
 * });
 */
export function useGetUserTasksByStatesQuery(baseOptions?: ApolloReactHooks.QueryHookOptions<GetUserTasksByStatesQuery, GetUserTasksByStatesQueryVariables>) {
        return ApolloReactHooks.useQuery<GetUserTasksByStatesQuery, GetUserTasksByStatesQueryVariables>(GetUserTasksByStatesDocument, baseOptions);
      }
export function useGetUserTasksByStatesLazyQuery(baseOptions?: ApolloReactHooks.LazyQueryHookOptions<GetUserTasksByStatesQuery, GetUserTasksByStatesQueryVariables>) {
          return ApolloReactHooks.useLazyQuery<GetUserTasksByStatesQuery, GetUserTasksByStatesQueryVariables>(GetUserTasksByStatesDocument, baseOptions);
        }
export type GetUserTasksByStatesQueryHookResult = ReturnType<typeof useGetUserTasksByStatesQuery>;
export type GetUserTasksByStatesLazyQueryHookResult = ReturnType<typeof useGetUserTasksByStatesLazyQuery>;
export type GetUserTasksByStatesQueryResult = ApolloReactCommon.QueryResult<GetUserTasksByStatesQuery, GetUserTasksByStatesQueryVariables>;
export const GetUserTaskByIdDocument = gql`
    query getUserTaskById($id: String) {
  UserTaskInstances(where: {id: {equal: $id}}) {
    id
    description
    name
    priority
    processInstanceId
    processId
    rootProcessInstanceId
    rootProcessId
    state
    actualOwner
    adminGroups
    adminUsers
    completed
    started
    excludedUsers
    potentialGroups
    potentialUsers
    inputs
    outputs
    referenceName
  }
}
    `;

/**
 * __useGetUserTaskByIdQuery__
 *
 * To run a query within a React component, call `useGetUserTaskByIdQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetUserTaskByIdQuery` returns an object from Apollo Client that contains loading, error, and data properties 
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetUserTaskByIdQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useGetUserTaskByIdQuery(baseOptions?: ApolloReactHooks.QueryHookOptions<GetUserTaskByIdQuery, GetUserTaskByIdQueryVariables>) {
        return ApolloReactHooks.useQuery<GetUserTaskByIdQuery, GetUserTaskByIdQueryVariables>(GetUserTaskByIdDocument, baseOptions);
      }
export function useGetUserTaskByIdLazyQuery(baseOptions?: ApolloReactHooks.LazyQueryHookOptions<GetUserTaskByIdQuery, GetUserTaskByIdQueryVariables>) {
          return ApolloReactHooks.useLazyQuery<GetUserTaskByIdQuery, GetUserTaskByIdQueryVariables>(GetUserTaskByIdDocument, baseOptions);
        }
export type GetUserTaskByIdQueryHookResult = ReturnType<typeof useGetUserTaskByIdQuery>;
export type GetUserTaskByIdLazyQueryHookResult = ReturnType<typeof useGetUserTaskByIdLazyQuery>;
export type GetUserTaskByIdQueryResult = ApolloReactCommon.QueryResult<GetUserTaskByIdQuery, GetUserTaskByIdQueryVariables>;
export const GetProcessInstanceByIdDocument = gql`
    query getProcessInstanceById($id: String) {
  ProcessInstances(where: {id: {equal: $id}}) {
    id
    processId
    processName
    endpoint
  }
}
    `;

/**
 * __useGetProcessInstanceByIdQuery__
 *
 * To run a query within a React component, call `useGetProcessInstanceByIdQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetProcessInstanceByIdQuery` returns an object from Apollo Client that contains loading, error, and data properties 
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetProcessInstanceByIdQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useGetProcessInstanceByIdQuery(baseOptions?: ApolloReactHooks.QueryHookOptions<GetProcessInstanceByIdQuery, GetProcessInstanceByIdQueryVariables>) {
        return ApolloReactHooks.useQuery<GetProcessInstanceByIdQuery, GetProcessInstanceByIdQueryVariables>(GetProcessInstanceByIdDocument, baseOptions);
      }
export function useGetProcessInstanceByIdLazyQuery(baseOptions?: ApolloReactHooks.LazyQueryHookOptions<GetProcessInstanceByIdQuery, GetProcessInstanceByIdQueryVariables>) {
          return ApolloReactHooks.useLazyQuery<GetProcessInstanceByIdQuery, GetProcessInstanceByIdQueryVariables>(GetProcessInstanceByIdDocument, baseOptions);
        }
export type GetProcessInstanceByIdQueryHookResult = ReturnType<typeof useGetProcessInstanceByIdQuery>;
export type GetProcessInstanceByIdLazyQueryHookResult = ReturnType<typeof useGetProcessInstanceByIdLazyQuery>;
export type GetProcessInstanceByIdQueryResult = ApolloReactCommon.QueryResult<GetProcessInstanceByIdQuery, GetProcessInstanceByIdQueryVariables>;
export const GetTaskForUserDocument = gql`
    query getTaskForUser($user: String, $groups: [String!], $offset: Int, $limit: Int) {
  UserTaskInstances(where: {or: [{actualOwner: {equal: $user}}, {potentialUsers: {contains: $user}}, {potentialGroups: {containsAny: $groups}}]}, pagination: {offset: $offset, limit: $limit}) {
    id
    name
    referenceName
    description
    priority
    processInstanceId
    processId
    rootProcessInstanceId
    rootProcessId
    state
    actualOwner
    adminGroups
    adminUsers
    completed
    started
    excludedUsers
    potentialGroups
    potentialUsers
    inputs
    outputs
    referenceName
    lastUpdate
  }
}
    `;

/**
 * __useGetTaskForUserQuery__
 *
 * To run a query within a React component, call `useGetTaskForUserQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetTaskForUserQuery` returns an object from Apollo Client that contains loading, error, and data properties 
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetTaskForUserQuery({
 *   variables: {
 *      user: // value for 'user'
 *      groups: // value for 'groups'
 *      offset: // value for 'offset'
 *      limit: // value for 'limit'
 *   },
 * });
 */
export function useGetTaskForUserQuery(baseOptions?: ApolloReactHooks.QueryHookOptions<GetTaskForUserQuery, GetTaskForUserQueryVariables>) {
        return ApolloReactHooks.useQuery<GetTaskForUserQuery, GetTaskForUserQueryVariables>(GetTaskForUserDocument, baseOptions);
      }
export function useGetTaskForUserLazyQuery(baseOptions?: ApolloReactHooks.LazyQueryHookOptions<GetTaskForUserQuery, GetTaskForUserQueryVariables>) {
          return ApolloReactHooks.useLazyQuery<GetTaskForUserQuery, GetTaskForUserQueryVariables>(GetTaskForUserDocument, baseOptions);
        }
export type GetTaskForUserQueryHookResult = ReturnType<typeof useGetTaskForUserQuery>;
export type GetTaskForUserLazyQueryHookResult = ReturnType<typeof useGetTaskForUserLazyQuery>;
export type GetTaskForUserQueryResult = ApolloReactCommon.QueryResult<GetTaskForUserQuery, GetTaskForUserQueryVariables>;