import gql from 'graphql-tag';

// @ts-ignore
const GET_USER_TASKS_BY_STATE = gql`
  query getUserTasksByState($state: String) {
    UserTaskInstances(where: { state: { equal: $state } }) {
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

// @ts-ignore
const GET_USER_TASKS_BY_STATES = gql`
  query getUserTasksByStates($state: [String!]) {
    UserTaskInstances(where: { state: { in: $state } }) {
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

// @ts-ignore
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
    }
  }
`;

// @ts-ignore
const GET_PROCESS_INSTANCE = gql`
  query getProcessInstanceById($id: String) {
    ProcessInstances(where: { id: { equal: $id } }) {
      id
      processId
      processName
      endpoint
    }
  }
`;

// @ts-ignore
const GET_TASKS_FOR_USER = gql`
  query getTaskForUser($user: String, $groups: [String!], $offset: Int, $limit: Int) {
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
      referenceName
      lastUpdate
    }
  }
`;
