/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
import { mount } from 'enzyme';
import WorkflowFormPage from '../WorkflowFormPage';
import { BrowserRouter } from 'react-router-dom';
import { WorkflowFormGatewayApiImpl } from '../../../../channel/WorkflowForm/WorkflowFormGatewayApi';
import * as WorkflowFormContext from '../../../../channel/WorkflowForm/WorkflowFormContext';
import { act } from 'react-dom/test-utils';

jest.mock('../../../containers/WorkflowFormContainer/WorkflowFormContainer');

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    location: {
      state: {
        workflowDefinition: {
          workflowName: 'workflow1',
          endpoint: 'http://localhost:4000'
        }
      }
    }
  })
}));

jest
  .spyOn(WorkflowFormContext, 'useWorkflowFormGatewayApi')
  .mockImplementation(() => new WorkflowFormGatewayApiImpl('baseUrl'));

describe('WorkflowFormPage tests', () => {
  it('Snapshot', () => {
    const wrapper = mount(
      <BrowserRouter>
        <WorkflowFormPage />
      </BrowserRouter>
    );

    expect(wrapper.find('WorkflowFormPage')).toMatchSnapshot();
    expect(wrapper.find('MockedWorkflowFormContainer').exists()).toBeTruthy();
  });

  it('test case for onSubmitSuccess prop',() => {
    let wrapper;
    act(() => {
      wrapper = mount(
        <BrowserRouter>
          <WorkflowFormPage />
        </BrowserRouter>
      );
    })
      const successMessage =  {
        type: 'success',
        message: 'A workflow with id undefined was triggered successfully.',
        details: undefined,
        customActions: [ { label: 'Go to workflow list', onClick: ['Function: onClick'] } ],
        close: ['Function: close']
      }
      wrapper
        .find('MockedWorkflowFormContainer')
        .props()
        ['onSubmitSuccess']();
      wrapper.update();
      expect( wrapper.find('FormNotification').props()['notification'].message).toEqual(successMessage.message)
  });

  it('test case for onSubmitError prop',() => {
    let wrapper;
    act(() => {
      wrapper = mount(
        <BrowserRouter>
          <WorkflowFormPage />
        </BrowserRouter>
      );
      })
      const errorMessage = {
        type: 'error',
        message: 'Failed to trigger workflow.',
        details: undefined,
        customActions: [ { label: 'Go to workflow list', onClick: ['Function: onClick'] } ],
        close: ['Function: close']
      }
      wrapper
        .find('MockedWorkflowFormContainer')
        .props()
        ['onSubmitError']();
      wrapper.update();
      expect( wrapper.find('FormNotification').props()['notification'].message).toEqual(errorMessage.message)
  });
});
