import gql from 'graphql-tag';

const GET_PROCESS_INSTANCES = gql`
  query getProcessInstances(
    $state: [ProcessInstanceState!]
    $offset: Int
    $limit: Int
  ) {
    ProcessInstances(
      where: {
        parentProcessInstanceId: { isNull: true }
        state: { in: $state }
      }
      pagination: { offset: $offset, limit: $limit }
    ) {
      id
      processId
      processName
      parentProcessInstanceId
      roles
      state
      start
      lastUpdate
      addons
      endpoint
      error {
        nodeDefinitionId
        message
      }
    }
  }
`;

const GET_ALL_PROCESS_INSTANCES = gql`
  query getAllProcessInstances($state: [ProcessInstanceState!]) {
    ProcessInstances(
      where: {
        parentProcessInstanceId: { isNull: true }
        state: { in: $state }
      }
    ) {
      id
      processId
      processName
      parentProcessInstanceId
      roles
      state
      start
      lastUpdate
      addons
      endpoint
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
      endpoint
      addons
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
      parentProcessInstanceId
      parentProcessInstance {
        id
        processName
      }
      roles
      variables
      state
      start
      lastUpdate
      end
      endpoint
      childProcessInstances {
        id
        processName
      }
      nodes {
        id
        name
        type
        enter
        exit
      }
    }
  }
`;
