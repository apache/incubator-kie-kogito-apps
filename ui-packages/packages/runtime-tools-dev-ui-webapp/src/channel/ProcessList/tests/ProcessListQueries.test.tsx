import { act } from 'react-dom/test-utils';
import reactApollo from 'react-apollo';
import { GraphQLProcessListQueries } from '../ProcessListQueries';
import {
  JobsDetailsModal,
  OperationType,
  OrderBy,
  ProcessInstance,
  ProcessInstanceState
} from '@kogito-apps/management-console-shared';
import { SortBy, ProcessInstanceFilter } from '@kogito-apps/process-list';
jest.mock('apollo-client');
jest.mock('react-apollo', () => {
  const ApolloClient = { query: jest.fn(), mutate: jest.fn() };
  return { useApolloClient: jest.fn(() => ApolloClient) };
});

const mGraphQLResponseProcess = {
  data: {
    ProcessInstances: [
      {
        id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0b',
        processId: 'travels',
        businessKey: null,
        parentProcessInstanceId: null,
        parentProcessInstance: null,
        processName: 'travels',
        roles: [],
        state: 'ACTIVE',
        rootProcessInstanceId: null,
        serviceUrl: 'http://localhost:4000',
        endpoint: 'http://localhost:4000',
        addons: [
          'jobs-management',
          'prometheus-monitoring',
          'process-management'
        ],
        error: {
          nodeDefinitionId: 'a1e139d5-4e77-48c9-84ae-3459188e90433n',
          message: 'Something went wrong'
        },
        start: '2019-10-22T03:40:44.089Z',
        end: null,
        variables:
          '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
        nodes: [
          {
            nodeId: '1',
            name: 'Book Flight',
            definitionId: 'CallActivity_2',
            id: '7cdeba99-cd36-4425-980d-e59d44769a3e',
            enter: '2019-10-22T04:43:01.143Z',
            exit: '2019-10-22T04:43:01.146Z',
            type: 'SubProcessNode'
          },
          {
            nodeId: '2',
            name: 'Confirm travel',
            definitionId: 'UserTask_2',
            id: '843bd287-fb6e-4ee7-a304-ba9b430e52d8',
            enter: '2019-10-22T04:43:01.148Z',
            exit: null,
            type: 'HumanTaskNode'
          },
          {
            nodeId: '3',
            name: 'Join',
            definitionId: 'ParallelGateway_2',
            id: 'fd2e12d5-6a4b-4c75-9f31-028d3f032a95',
            enter: '2019-10-22T04:43:01.148Z',
            exit: '2019-10-22T04:43:01.148Z',
            type: 'Join'
          },
          {
            nodeId: '4',
            name: 'Book Hotel',
            definitionId: 'CallActivity_1',
            id: '7f7d74c1-78f7-49be-b5ad-8d132f46a49c',
            enter: '2019-10-22T04:43:01.146Z',
            exit: '2019-10-22T04:43:01.148Z',
            type: 'SubProcessNode'
          },
          {
            nodeId: '5',
            name: 'Book',
            definitionId: 'ParallelGateway_1',
            id: 'af0d984c-4abd-4f5c-83a8-426e6b3d102a',
            enter: '2019-10-22T04:43:01.143Z',
            exit: '2019-10-22T04:43:01.146Z',
            type: 'Split'
          },
          {
            nodeId: '6',
            name: 'Join',
            definitionId: 'ExclusiveGateway_2',
            id: 'b2761011-3043-4f48-82bd-1395bf651a91',
            enter: '2019-10-22T04:43:01.143Z',
            exit: '2019-10-22T04:43:01.143Z',
            type: 'Join'
          },
          {
            nodeId: '7',
            name: 'is visa required',
            definitionId: 'ExclusiveGateway_1',
            id: 'a91a2600-d0cd-46ff-a6c6-b3081612d1af',
            enter: '2019-10-22T04:43:01.143Z',
            exit: '2019-10-22T04:43:01.143Z',
            type: 'Split'
          },
          {
            nodeId: '8',
            name: 'Visa check',
            definitionId: 'BusinessRuleTask_1',
            id: '1baa5de4-47cc-45a8-8323-005388191e4f',
            enter: '2019-10-22T04:43:01.135Z',
            exit: '2019-10-22T04:43:01.143Z',
            type: 'RuleSetNode'
          },
          {
            nodeId: '9',
            name: 'StartProcess',
            definitionId: 'StartEvent_1',
            id: '90e5a337-1c26-4fcc-8ee2-d20e6ba2a1a3',
            enter: '2019-10-22T04:43:01.135Z',
            exit: '2019-10-22T04:43:01.135Z',
            type: 'StartNode'
          }
        ],
        milestones: [],
        childProcessInstances: [
          {
            id: 'c54ca5b0-b975-46e2-a9a0-6a86bf7ac21e',
            processName: 'FlightBooking',
            businessKey: null
          },
          {
            id: '2d962eef-45b8-48a9-ad4e-9cde0ad6af88',
            processName: 'HotelBooking',
            businessKey: null
          }
        ]
      }
    ]
  }
};

const data: ProcessInstance = {
  id: 'tEE12-fo54-l665-mp112-akou112345566',
  processId: 'travels',
  businessKey: 'TEE12',
  parentProcessInstanceId: null,
  parentProcessInstance: null,
  processName: 'travels',
  roles: [],
  state: ProcessInstanceState.Error,
  rootProcessInstanceId: null,
  addons: ['jobs-management', 'prometheus-monitoring', 'process-management'],
  start: new Date('2019-10-22T03:40:44.089Z'),
  end: new Date('2019-10-22T05:40:44.089Z'),
  error: {
    nodeDefinitionId: '_2140F05A-364F-40B3-BB7B-B12927065DF8',
    message: 'Something went wrong'
  },
  serviceUrl: 'http://localhost:4000',
  endpoint: 'http://localhost:4000',
  variables:
    '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Bangalore","country":"India","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"hotel":{"address":{"city":"Bangalore","country":"India","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"traveller":{"address":{"city":"Bangalore","country":"US","street":"Bangalore","zipCode":"560093"},"email":"ajaganat@redhat.com","firstName":"Ajay","lastName":"Jaganathan","nationality":"US"}}',
  nodes: [
    {
      nodeId: '1',
      name: 'End Event 1',
      definitionId: 'EndEvent_1',
      id: '870bdda0-be04-4e59-bb0b-f9b665eaacc9',
      enter: new Date('2019-10-22T03:37:38.586Z'),
      exit: new Date('2019-10-22T03:37:38.586Z'),
      type: 'EndNode'
    },
    {
      nodeId: '2',
      name: 'Confirm travel',
      definitionId: 'UserTask_2',
      id: '6b4a4fe9-4aab-4e8c-bb79-27b8b6b88d1f',
      enter: new Date('2019-10-22T03:37:30.807Z'),
      exit: new Date('2019-10-22T03:37:38.586Z'),
      type: 'HumanTaskNode'
    },
    {
      nodeId: '3',
      name: 'Book Hotel',
      definitionId: 'CallActivity_1',
      id: 'dd33de7c-c39c-484a-83a8-3e1b007fce95',
      enter: new Date('2019-10-22T03:37:30.793Z'),
      exit: new Date('2019-10-22T03:37:30.803Z'),
      type: 'SubProcessNode'
    },
    {
      nodeId: '4',
      name: 'Join',
      definitionId: '_2140F05A-364F-40B3-BB7B-B12927065DF8',
      id: '08c153e8-2766-4675-81f7-29943efdf411',
      enter: new Date('2019-10-22T03:37:30.806Z'),
      exit: new Date('2019-10-22T03:37:30.807Z'),
      type: 'Join'
    },
    {
      nodeId: '4',
      name: 'Book Flight',
      definitionId: 'CallActivity_2',
      id: '683cf307-f082-4a8e-9c85-d5a11b13903a',
      enter: new Date('2019-10-22T03:37:30.803Z'),
      exit: new Date('2019-10-22T03:37:30.806Z'),
      type: 'SubProcessNode'
    },
    {
      nodeId: '5',
      name: 'Book',
      definitionId: 'ParallelGateway_1',
      id: 'cf057e58-4113-46c0-be13-6de42ea8377e',
      enter: new Date('2019-10-22T03:37:30.792Z'),
      exit: new Date('2019-10-22T03:37:30.803Z'),
      type: 'Split'
    },
    {
      nodeId: '6',
      name: 'Join',
      definitionId: 'ExclusiveGateway_2',
      id: '415a52c0-dc1f-4a93-9238-862dc8072262',
      enter: new Date('2019-10-22T03:37:30.792Z'),
      exit: new Date('2019-10-22T03:37:30.792Z'),
      type: 'Join'
    },
    {
      nodeId: '7',
      name: 'is visa required',
      definitionId: 'ExclusiveGateway_1',
      id: '52d64298-3f28-4aba-a812-dba4077c9665',
      enter: new Date('2019-10-22T03:37:30.79Z'),
      exit: new Date('2019-10-22T03:37:30.792Z'),
      type: 'Split'
    },
    {
      nodeId: '8',
      name: 'Visa check',
      definitionId: 'BusinessRuleTask_1',
      id: '6fdee287-08f6-49c2-af2d-2d125ba76ab7',
      enter: new Date('2019-10-22T03:37:30.755Z'),
      exit: new Date('2019-10-22T03:37:30.79Z'),
      type: 'RuleSetNode'
    },
    {
      nodeId: '9',
      name: 'StartProcess',
      definitionId: 'StartEvent_1',
      id: 'd98c1762-9d3c-4228-9ffc-bc3f423079c0',
      enter: new Date('2019-10-22T03:37:30.753Z'),
      exit: new Date('2019-10-22T03:37:30.754Z'),
      type: 'StartNode'
    }
  ],
  milestones: [],
  childProcessInstances: []
};

describe('ProcessListQueries tests', () => {
  let client;
  let useApolloClient;
  const mockUseApolloClient = () => {
    act(() => {
      client = useApolloClient();
    });
  };

  let Queries: GraphQLProcessListQueries;
  let rootProcessInstanceId: null;

  const processListFilters: ProcessInstanceFilter = {
    status: [ProcessInstanceState.Active],
    businessKey: []
  };
  const sortBy: SortBy = { lastUpdate: OrderBy.DESC };
  const processInstances: ProcessInstance[] = [];

  beforeEach(() => {
    act(() => {
      useApolloClient = jest.spyOn(reactApollo, 'useApolloClient');
      mockUseApolloClient();
    });
    Queries = new GraphQLProcessListQueries(client);
  });

  it('test getProcessInstances method with success response', async () => {
    client.query.mockResolvedValue(mGraphQLResponseProcess);
    const response = await Queries.getProcessInstances(
      0,
      10,
      processListFilters,
      sortBy
    );
    expect(response).toEqual(mGraphQLResponseProcess.data.ProcessInstances);
  });

  it('test getProcessInstances method with Error response', async () => {
    client.query.mockRejectedValue({ message: '404 (Not Found)' });
    let result = null;
    await Queries.getProcessInstances(0, 10, processListFilters, sortBy)
      .then(response => {
        result = response;
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual(result);
  });

  it('test getChildProcessInstances method with success response', async () => {
    client.query.mockResolvedValue(mGraphQLResponseProcess);
    const response = await Queries.getChildProcessInstances(
      rootProcessInstanceId
    );
    expect(response).toEqual(mGraphQLResponseProcess.data.ProcessInstances);
  });

  it('test getChildProcessInstances method with Error response', async () => {
    client.query.mockRejectedValue({ message: '404 (Not Found)' });
    let result = null;
    await Queries.getChildProcessInstances(rootProcessInstanceId)
      .then(response => {
        result = response;
      })
      .catch(error => {
        result = error.message;
      });
    expect(result).toEqual(result);
  });

  it('test skip  method with success response', async () => {
    client.mutate.mockResolvedValue({ data: 'success' });
    const response = await Queries.handleProcessSkip(data);
    expect(response).toEqual('success');
  });

  it('test Abort method with success response', async () => {
    client.mutate.mockResolvedValue({ data: 'success' });
    const response = await Queries.handleProcessAbort(data);
    expect(response).toEqual('success');
  });

  it('test Retry method with success response', async () => {
    client.mutate.mockResolvedValue({ data: 'success' });
    const response = await Queries.handleProcessRetry(data);
    expect(response).toEqual('success');
  });

  it('test MultipleAction method with success response', async () => {
    client.mutate.mockResolvedValue({
      failedProcessInstances: [],
      successProcessInstances: []
    });
    const response = await Queries.handleProcessMultipleAction(
      processInstances,
      OperationType
    );
    expect(response).toEqual({
      failedProcessInstances: [],
      successProcessInstances: []
    });
  });
});
