/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import gql from 'graphql-tag';

const GET_PROCESS_INSTANCES = gql`
  query getProcessInstances(
    $where: ProcessInstanceArgument
    $offset: Int
    $limit: Int
  ) {
    ProcessInstances(
      where: $where
      pagination: { offset: $offset, limit: $limit }
    ) {
      id
      processId
      processName
      parentProcessInstanceId
      rootProcessInstanceId
      roles
      state
      start
      lastUpdate
      addons
      businessKey
      serviceUrl
      error {
        nodeDefinitionId
        message
      }
    }
  }
`;

const GET_CHILD_INSTANCES = gql`
  query getChildInstances($rootProcessInstanceId: String) {
    ProcessInstances(
      where: { rootProcessInstanceId: { equal: $rootProcessInstanceId } }
    ) {
      id
      processId
      processName
      parentProcessInstanceId
      rootProcessInstanceId
      roles
      state
      start
      lastUpdate
      serviceUrl
      addons
      businessKey
      error {
        nodeDefinitionId
        message
      }
    }
  }
`;

const GET_PROCESS_INSTANCE = gql`
  query getProcessInstanceById($id: String) {
    ProcessInstances(where: { id: { equal: $id } }) {
      id
      processId
      processName
      businessKey
      parentProcessInstanceId
      parentProcessInstance {
        id
        processName
        businessKey
      }
      roles
      variables
      state
      start
      lastUpdate
      end
      addons
      endpoint
      addons
      serviceUrl
      error {
        nodeDefinitionId
        message
      }
      childProcessInstances {
        id
        processName
        businessKey
      }
      nodes {
        id
        nodeId
        name
        enter
        exit
        type
        definitionId
      }
      milestones {
        id
        name
        status
      }
    }
  }
`;

const GET_COLUMN_PICKER_ATTRIBUTES = gql`
  query getColumnPickerAttributes($columnPickerType: String!) {
    __type(name: $columnPickerType) {
      name
      fields {
        name
        type {
          name
          kind
          fields {
            name
            type {
              name
              kind
            }
          }
        }
      }
    }
  }
`;

const GET_QUERY_TYPES = gql`
  query getQueryTypes {
    __schema {
      queryType: types {
        name
        kind
        fields {
          name
          type {
            name
            kind
          }
        }
        inputFields {
          name
          type {
            name
            kind
          }
        }
      }
    }
  }
`;

const GET_QUERY_FIELDS = gql`
  query getQueryFields {
    __type(name: "Query") {
      name
      fields {
        name
        args {
          name
          type {
            kind
            name
          }
        }
        type {
          ofType {
            name
          }
        }
      }
    }
  }
`;

const GET_INPUT_FIELDS_FROM_QUERY = gql`
  query getInputFieldsFromQuery($currentQuery: String!) {
    __type(name: $currentQuery) {
      name
      inputFields {
        name
        type {
          name
          kind
          inputFields {
            name
            type {
              name
            }
          }
        }
      }
    }
  }
`;

const GET_INPUT_FIELDS_FROM_TYPES = gql`
  query getInputFieldsFromType($type: String!) {
    __type(name: $type) {
      name
      inputFields {
        name
        type {
          name
          kind
          enumValues {
            name
          }
          ofType {
            kind
            name
            enumValues {
              name
            }
          }
        }
      }
    }
  }
`;

const GET_USER_TASKS_BY_STATES = gql`
  query getUserTasksByStates(
    $state: [String!]
    $orderBy: UserTaskInstanceOrderBy
  ) {
    UserTaskInstances(where: { state: { in: $state } }, orderBy: $orderBy) {
      id
      name
      referenceName
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
      endpoint
    }
  }
`;

const GET_USER_TASK = gql`
  query getUserTaskById($id: String) {
    UserTaskInstances(where: { id: { equal: $id } }) {
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
      endpoint
    }
  }
`;

const GET_TASKS_FOR_USER = gql`
  query getTasksForUser(
    $user: String
    $groups: [String!]
    $offset: Int
    $limit: Int
  ) {
    UserTaskInstances(
      where: {
        or: [
          { actualOwner: { equal: $user } }
          { potentialUsers: { contains: $user } }
          { potentialGroups: { containsAny: $groups } }
        ]
      }
      pagination: { offset: $offset, limit: $limit }
    ) {
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
      lastUpdate
      endpoint
    }
  }
`;

const GET_JOBS_BY_PROC_INST_ID = gql`
  query getJobsByProcessInstanceId($processInstanceId: String) {
    Jobs(where: { processInstanceId: { equal: $processInstanceId } }) {
      id
      processId
      processInstanceId
      rootProcessId
      status
      expirationTime
      priority
      callbackEndpoint
      repeatInterval
      repeatLimit
      scheduledId
      retries
      lastUpdate
      expirationTime
    }
  }
`;
