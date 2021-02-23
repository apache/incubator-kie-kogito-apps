import { getWrapper, GraphQL, KogitoSpinner } from '@kogito-apps/common';
import React from 'react';
import ProcessListTable from '../ProcessListTable';
import { BrowserRouter } from 'react-router-dom';
import { Button, Checkbox } from '@patternfly/react-core';
import _ from 'lodash';
import { act } from 'react-test-renderer';
jest.mock('../../../Molecules/SubProcessTable/SubProcessTable');

const data = {
  ProcessInstances: [
    {
      id: '538f9feb-5a14-4096-b791-2055b38da7c6',
      processId: 'travels',
      businessKey: 'Tra234',
      parentProcessInstanceId: null,
      parentProcessInstance: null,
      processName: 'travels',
      rootProcessInstanceId: null,
      roles: [],
      state: GraphQL.ProcessInstanceState.Error,
      addons: [
        'jobs-management',
        'prometheus-monitoring',
        'process-management'
      ],
      start: '2019-10-22T03:40:44.089Z',
      error: {
        nodeDefinitionId: '__a1e139d5-4e77-48c9-84ae-34578e9817n',
        message: 'Something went wrong'
      },
      lastUpdate: '2019-10-22T03:40:44.089Z',
      serviceUrl: 'http://localhost:4000',
      endpoint: 'http://localhost:4000',
      variables:
        '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-23T22:00:00Z[UTC]","flightNumber":"MX555"},"trip":{"begin":"2019-10-23T22:00:00Z[UTC]","city":"New York","country":"US","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"hotel":{"address":{"city":"New York","country":"US","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"traveller":{"address":{"city":"Berlin","country":"Germany","street":"Bakers","zipCode":"100200"},"email":"cristiano@redhat.com","firstName":"Cristiano","lastName":"Nicolai","nationality":"German"}}',
      nodes: [],
      milestones: [],
      isSelected: false,
      childProcessInstances: [
        {
          id: 'c54ca5b0-b975-46e2-a9a0-6a86bf7ac21eajabbcc',
          processId: 'travels',
          businessKey: 'TP444',
          parentProcessInstanceId: '538f9feb-5a14-4096-b791-2055b38da7c6',
          parentProcessInstance: null,
          rootProcessInstanceId: '538f9feb-5a14-4096-b791-2055b38da7c6',
          processName: 'FlightBooking test 2',
          roles: [],
          state: GraphQL.ProcessInstanceState.Active,
          serviceUrl: 'http://localhost:4000',
          lastUpdate: '2019-10-22T03:40:44.089Z',
          endpoint: 'http://localhost:4000',
          addons: ['process-management'],
          error: {
            nodeDefinitionId: 'a1e139d5-81c77-48c9-84ae-34578e90433n',
            message: 'Something went wrong'
          },
          start: '2019-10-22T03:40:44.089Z',
          end: '2019-10-22T05:40:44.089Z',
          variables:
            '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
          nodes: [],
          isSelected: true,
          milestones: [],
          childProcessInstances: []
        }
      ]
    },
    {
      id: '538f9feb-5a14-4096-b791-2055b38da7c6',
      processId: 'travels',
      businessKey: 'Tra234',
      parentProcessInstanceId: null,
      parentProcessInstance: null,
      processName: 'travels',
      rootProcessInstanceId: null,
      roles: [],
      state: GraphQL.ProcessInstanceState.Error,
      addons: [
        'jobs-management',
        'prometheus-monitoring',
        'process-management'
      ],
      start: '2019-10-22T03:40:44.089Z',
      error: {
        nodeDefinitionId: '__a1e139d5-4e77-48c9-84ae-34578e9817n',
        message: 'Something went wrong'
      },
      lastUpdate: '2019-10-22T03:40:44.089Z',
      serviceUrl: 'http://localhost:4000',
      endpoint: 'http://localhost:4000',
      variables:
        '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-23T22:00:00Z[UTC]","flightNumber":"MX555"},"trip":{"begin":"2019-10-23T22:00:00Z[UTC]","city":"New York","country":"US","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"hotel":{"address":{"city":"New York","country":"US","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"traveller":{"address":{"city":"Berlin","country":"Germany","street":"Bakers","zipCode":"100200"},"email":"cristiano@redhat.com","firstName":"Cristiano","lastName":"Nicolai","nationality":"German"}}',
      nodes: [],
      milestones: [],
      isSelected: true,
      childProcessInstances: ['538f9feb-5a14-4096-b791-2055b38da7c6child']
    }
  ]
};

const props = {
  initData: data,
  setInitData: jest.fn(),
  loading: false,
  filters: {
    status: [GraphQL.ProcessInstanceState.Active],
    businessKey: []
  },
  expanded: {
    0: false,
    1: false
  },
  setExpanded: jest.fn(),
  setSelectedInstances: jest.fn(),
  selectedInstances: [],
  setSelectableInstances: jest.fn(),
  setIsAllChecked: jest.fn(),
  selectableInstances: 0
};

describe('ProcessListPage tests', () => {
  it('snapshot test for process list - without expanded', () => {
    const wrapper = getWrapper(
      <BrowserRouter>
        <ProcessListTable {...props} />
      </BrowserRouter>,
      'ProcessListTable'
    );
    expect(wrapper).toMatchSnapshot();
  });
  it('snapshot test for disabled process and non "error" state', () => {
    const clonedProps = _.cloneDeep(props);
    clonedProps.initData.ProcessInstances[0].state =
      GraphQL.ProcessInstanceState.Active;
    clonedProps.initData.ProcessInstances[0].addons = [];
    clonedProps.initData.ProcessInstances[0].serviceUrl = null;
    const wrapper = getWrapper(
      <BrowserRouter>
        <ProcessListTable {...clonedProps} />
      </BrowserRouter>,
      'ProcessListTable'
    );
    expect(wrapper).toMatchSnapshot();
  });
  it('snapshot for loading state', () => {
    const wrapper = getWrapper(
      <BrowserRouter>
        <ProcessListTable {...{ ...props, loading: true, initData: {} }} />
      </BrowserRouter>,
      'ProcessListTable'
    );
    const spinner = wrapper.find(KogitoSpinner);
    expect(spinner.exists()).toBeTruthy();
    expect(spinner).toMatchSnapshot();
  });
  it('snapshot test for process list - with expanded', async () => {
    const clonedProps = _.cloneDeep(props);
    clonedProps.expanded = {
      0: true,
      1: false
    };
    clonedProps.selectedInstances = [
      {
        id: 'c54ca5b0-b975-46e2-a9a0-6a86bf7ac21eajabbcc',
        processId: 'travels',
        businessKey: 'TP444',
        parentProcessInstanceId: '538f9feb-5a14-4096-b791-2055b38da7c6',
        parentProcessInstance: null,
        rootProcessInstanceId: '538f9feb-5a14-4096-b791-2055b38da7c6',
        processName: 'FlightBooking test 2',
        roles: [],
        state: GraphQL.ProcessInstanceState.Completed,
        serviceUrl: null,
        lastUpdate: '2019-10-22T03:40:44.089Z',
        endpoint: 'http://localhost:4000',
        addons: [],
        error: {
          nodeDefinitionId: 'a1e139d5-81c77-48c9-84ae-34578e90433n',
          message: 'Something went wrong'
        },
        start: '2019-10-22T03:40:44.089Z',
        end: '2019-10-22T05:40:44.089Z',
        variables:
          '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
        nodes: [],
        isSelected: true,
        milestones: [],
        childProcessInstances: []
      }
    ];
    clonedProps.selectableInstances = 1;

    const wrapper = getWrapper(
      <BrowserRouter>
        <ProcessListTable {...clonedProps} />
      </BrowserRouter>,
      'ProcessListTable'
    );
    await act(async () => {
      wrapper
        .find('CollapseColumn')
        .at(0)
        .find(Button)
        .simulate('click');
    });
    const SubProcessTable = wrapper.update().find('MockedSubProcessTable');
    expect(SubProcessTable.exists()).toBeTruthy();
    expect(SubProcessTable).toMatchSnapshot();
  });
  it('checkbox click tests - selected', async () => {
    const clonedProps = _.cloneDeep(props);
    let wrapper = getWrapper(
      <BrowserRouter>
        <ProcessListTable {...clonedProps} />
      </BrowserRouter>,
      'ProcessListTable'
    );
    await act(async () => {
      wrapper
        .find(Checkbox)
        .at(0)
        .find('input')
        .simulate('change', { target: { checked: true } });
    });
    wrapper = wrapper.update();
    expect(props.setSelectedInstances).toHaveBeenCalled();
  });
  it('checkbox click tests - unselected', async () => {
    let wrapper = getWrapper(
      <BrowserRouter>
        <ProcessListTable {...props} />
      </BrowserRouter>,
      'ProcessListTable'
    );
    await act(async () => {
      wrapper
        .find(Checkbox)
        .at(1)
        .find('input')
        .simulate('change', { target: { checked: false } });
    });
    wrapper = wrapper.update();
    expect(props.setSelectedInstances).toHaveBeenCalled();
  });
});
