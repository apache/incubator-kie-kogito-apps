import gql from 'graphql-tag';

const GET_PROCESS_INSTANCES = gql`
  query getProcessInstances($state: [ProcessInstanceState!]) {
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
  query getChildInstances($instanceId: String) {
    ProcessInstances(
      where: { parentProcessInstanceId: { equal: $instanceId } }
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

const GET_PICKER = gql`
  query getPicker($columnPickerType: String!) {
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

const GET_QUERY = gql`
  query getQuery {
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

const GET_SCHEMA = gql`
  query getSchema($currentQuery: String!) {
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

const GET_TYPES = gql`
  query getTypes($currentArgument: String!) {
    __type(name: $currentArgument) {
      name
      inputFields {
        name
        type {
          name
          kind
        }
      }
    }
  }
`;
