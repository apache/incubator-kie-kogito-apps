/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import { shallow } from 'enzyme';
import DisablePopup from '../DisablePopup';
import { Checkbox } from '@patternfly/react-core';
import { ProcessInstanceState } from '@kogito-apps/management-console-shared';

const props1 = {
  processInstanceData: {
    id: '8035b580-6ae4-4aa8-9ec0-e18e19809e0blmnop',
    processId: 'travels',
    businessKey: 'Tr1122',
    parentProcessInstanceId: null,
    parentProcessInstance: null,
    processName: 'travels',
    roles: [],
    state: ProcessInstanceState.Active,
    rootProcessInstanceId: null,
    serviceUrl: null,
    endpoint: 'http://localhost:4000',
    addons: ['process-management'],
    error: {
      nodeDefinitionId: 'a1e139d5-4e77-48c9-84ae-3459188e90433n',
      message: 'Something went wrong'
    },
    start: new Date('2019-12-22T03:40:44.089Z'),
    lastUpdate: new Date('2019-12-22T03:40:44.089Z'),
    end: null,
    variables:
      '{"flight":{"arrival":"2019-10-30T22:00:00Z[UTC]","departure":"2019-10-22T22:00:00Z[UTC]","flightNumber":"MX555"},"hotel":{"address":{"city":"Berlin","country":"Germany","street":"street","zipCode":"12345"},"bookingNumber":"XX-012345","name":"Perfect hotel","phone":"09876543"},"trip":{"begin":"2019-10-22T22:00:00Z[UTC]","city":"Berlin","country":"Germany","end":"2019-10-30T22:00:00Z[UTC]","visaRequired":false},"traveller":{"address":{"city":"Karkow","country":"Poland","street":"palna","zipCode":"200300"},"email":"rob@redhat.com","firstName":"Rob","lastName":"Rob","nationality":"Polish"}}',
    nodes: [
      {
        nodeId: '1',
        name: 'Book Flight',
        definitionId: 'CallActivity_2',
        id: '7cdeba99-cd36-4425-980d-e59d44769a3e',
        enter: new Date('2019-10-22T04:43:01.143Z'),
        exit: new Date('2019-10-22T04:43:01.146Z'),
        type: 'SubProcessNode'
      }
    ],
    childProcessInstances: []
  },
  component: <Checkbox id="test" />
};

describe('DisablePopup component tests', () => {
  it('snapshot testing for no service URL and only process-management addon', () => {
    const wrapper = shallow(<DisablePopup {...props1} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('snapshot testing for no service URL and no process-management addon', () => {
    const wrapper = shallow(
      <DisablePopup
        {...{
          ...props1,
          processInstanceData: { ...props1.processInstanceData, addons: [] }
        }}
      />
    );
    expect(wrapper).toMatchSnapshot();
  });
  it('snapshot testing for service URL and process-management addon available', () => {
    const wrapper = shallow(
      <DisablePopup
        {...{
          ...props1,
          processInstanceData: {
            ...props1.processInstanceData,
            addons: ['process-management'],
            serviceUrl: 'http://localhost:4000'
          }
        }}
      />
    );
    expect(wrapper).toMatchSnapshot();
  });
  it('snapshot testing for no process-management addon and only service URL', () => {
    const wrapper = shallow(
      <DisablePopup
        {...{
          ...props1,
          processInstanceData: {
            ...props1.processInstanceData,
            addons: [],
            serviceUrl: 'http://localhost:4000'
          }
        }}
      />
    );
    expect(wrapper).toMatchSnapshot();
  });
});
