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
import ProcessFormContainer from '../ProcessFormContainer';
import * as FormDetailsContext from '../../../../channel/FormDetails/FormDetailsContext';
import { FormDetailsGatewayApiImpl } from '../../../../channel/FormDetails/FormDetailsGatewayApi';

jest
  .spyOn(FormDetailsContext, 'useFormDetailsGatewayApi')
  .mockImplementation(() => new FormDetailsGatewayApiImpl());

describe('ProcessFormContainer tests', () => {
  it('Snapshot', () => {
    const props = {
      processDefinitionData: {
        processName: 'process1',
        endpoint: 'http://localhost:4000'
      },
      onSubmitSuccess: jest.fn(),
      onSubmitError: jest.fn()
    };
    const wrapper = mount(<ProcessFormContainer {...props} />);

    expect(wrapper).toMatchSnapshot();

    const forwardRef = wrapper.childAt(0);

    expect(forwardRef.props().driver).not.toBeNull();

    expect(forwardRef.props().targetOrigin).toBe('*');
  });
});
