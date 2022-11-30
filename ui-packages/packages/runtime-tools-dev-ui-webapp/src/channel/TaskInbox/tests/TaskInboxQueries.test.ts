import { act } from 'react-dom/test-utils';
import reactApollo from 'react-apollo';
import { GraphQLTaskInboxQueries } from '../TaskInboxQueries';
import { DefaultUser, User } from '@kogito-apps/consoles-common';
import { QueryFilter, SortBy, TaskInboxState } from '@kogito-apps/task-inbox';
jest.mock('apollo-client');

jest.mock('react-apollo', () => {
  const ApolloClient = { query: jest.fn(), mutate: jest.fn() };
  return { useApolloClient: jest.fn(() => ApolloClient) };
});

const mGraphQLResponseProcess = {
  data: {
    UserTaskInstances: [
      {
        id: '45a73767-5da3-49bf-9c40-d533c3e77ef3',
        description: null,
        name: 'VisaApplication',
        priority: '1',
        processInstanceId: '9ae7ce3b-d49c-4f35-b843-8ac3d22fa427',
        processId: 'travels',
        rootProcessInstanceId: null,
        rootProcessId: null,
        state: 'Ready',
        actualOwner: 'john',
        adminGroups: [],
        adminUsers: [],
        completed: null,
        started: '2020-02-19T11:11:56.282Z',
        excludedUsers: [],
        potentialGroups: [],
        potentialUsers: [],
        inputs:
          '{"Skippable":"true","trip":{"city":"Boston","country":"US","begin":"2020-02-19T23:00:00.000+01:00","end":"2020-02-26T23:00:00.000+01:00","visaRequired":true},"TaskName":"VisaApplication","NodeName":"Apply for visa","traveller":{"firstName":"Rachel","lastName":"White","email":"rwhite@gorle.com","nationality":"Polish","address":{"street":"Cabalone","city":"Zerf","zipCode":"765756","country":"Poland"}},"Priority":"1"}',
        outputs: '{}',
        referenceName: 'Apply for visa (Empty Form)',
        lastUpdate: '2020-02-19T11:11:56.282Z',
        endpoint:
          'http://localhost:4000/travels/9ae7ce3b-d49c-4f35-b843-8ac3d22fa427/VisaApplication/45a73767-5da3-49bf-9c40-d533c3e77ef3'
      }
    ]
  }
};

describe('TaskInboxQueries tests', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  let Queries: GraphQLTaskInboxQueries;
  const taskId = '45a73767-5da3-49bf-9c40-d533c3e77ef3';
  const user: User = new DefaultUser('jon snow', ['hero']);

  const filters: QueryFilter = {
    taskNames: [],
    taskStates: []
  };
  const sortBy: SortBy = {
    property: 'lastUpdate',
    direction: 'asc'
  };

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
    Queries = new GraphQLTaskInboxQueries(client);
  });

  it('test getUserTaskById method with success response', async () => {
    client.query.mockResolvedValue(mGraphQLResponseProcess);
    const response = await Queries.getUserTaskById(taskId);
    expect(response).toEqual(mGraphQLResponseProcess.data.UserTaskInstances[0]);
  });

  it('test getUserTaskById method with Error response', async () => {
    client.query.mockRejectedValue({ message: '404 (Not Found)' });
    let result = null;
    await Queries.getUserTaskById(taskId)
      .then(response => {
        result = response;
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual(result);
  });

  it('test getUserTasks method with success response', async () => {
    client.query.mockResolvedValue(mGraphQLResponseProcess);
    const response = await Queries.getUserTasks(user, 0, 10, filters, sortBy);
    expect(response).toEqual(mGraphQLResponseProcess.data.UserTaskInstances);
  });

  it('test getUserTasks method with Error response', async () => {
    client.query.mockRejectedValue({ message: '404 (Not Found)' });
    let result = null;
    await Queries.getUserTasks(user, 0, 10, filters, sortBy)
      .then(response => {
        result = response;
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual(result);
  });
});
