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
import { mount } from 'enzyme';
import ProcessFormPage from '../ProcessFormPage';
import { BrowserRouter } from 'react-router-dom';
import * as ProcessFormContext from '../../../../channel/ProcessForm/ProcessFormContext';
import {
  ProcessFormGatewayApi,
  ProcessFormGatewayApiImpl
} from '../../../../channel/ProcessForm/ProcessFormGatewayApi';
import { JobsDetailsModal } from '@kogito-apps/management-console-shared';
import { hasUncaughtExceptionCaptureCallback } from 'process';
import ProcessFormContainer from '../../../containers/ProcessFormContainer/ProcessFormContainer';
import { act } from 'react-dom/test-utils';

jest.mock('../components/InlineEdit/InlineEdit');
jest.mock('../../../containers/ProcessFormContainer/ProcessFormContainer');

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    location: {
      state: {
        processDefinition: {
          processName: 'process1',
          endpoint: 'http://localhost:4000'
        }
      }
    }
  })
}));

describe('ProcessFormPage tests', () => {
  it('Snapshot', () => {
    const wrapper = mount(
      <BrowserRouter>
        <ProcessFormPage />
      </BrowserRouter>
    );
    expect(wrapper.find('ProcessFormPage')).toMatchSnapshot();
    expect(wrapper.find('MockedProcessFormContainer').exists()).toBeTruthy();
  });

  it('test case for onSubmitSuccess prop',() => {
    let wrapper;
     act(() => {
      wrapper = mount(
        <BrowserRouter>
          <ProcessFormPage />
        </BrowserRouter>
      );
     })
     
    const successMessage = {
        type: 'success',
        message: 'The process with id: undefined has started successfully',
        details: undefined,
        customActions: [
          { label: 'Go to process list', onClick: ['Function: onClick'] },
          { label: 'Go to Process details', onClick:['Function: onClick'] }
        ],
        close:['Function: close']
      };
  
   wrapper
    .find('MockedProcessFormContainer')
    .props()
     ['onSubmitSuccess']()

  wrapper.update()
  expect(wrapper.find('FormNotification').props()['notification'].message).toEqual(successMessage.message);
  })

  it('test case for onSubmitError prop', () => {
    let wrapper;
    act(() => {
      wrapper = mount(
        <BrowserRouter>
          <ProcessFormPage />
        </BrowserRouter>
      )
    })

    const errorMessage = {
        type: 'error',
        message: 'Failed to start the process.',
        details: undefined,
        customActions: [
          { label: 'Go to process list', onClick: ['Function: onClick'] },
          { label: 'Go to Process details', onClick:['Function: onClick'] }
        ],
        close:['Function: close']
      };
      wrapper
        .find('MockedProcessFormContainer')
        .props()
        ['onSubmitError']();
     wrapper.update();
     expect( wrapper.find('FormNotification').props()['notification'].message).toEqual(errorMessage.message);
  });
});
