/**
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import { mount } from 'enzyme';
import FormFooter from '../FormFooter';
import { IFormAction } from '../../../../util/uniforms/FormSubmitHandler/FormSubmitHandler';

describe('Form Footer test', () => {
  it('testing showing actions', () => {
    const actions: IFormAction[] = [
      {
        name: 'action1',
        primary: true,
        execute: jest.fn()
      },
      {
        name: 'action2',
        execute: jest.fn()
      }
    ];

    const wrapper = mount(<FormFooter actions={actions} />);
    expect(wrapper).toMatchSnapshot();
  });

  it('testing showing empty actions', () => {
    const props = {
      actions: []
    };

    const wrapper = mount(<FormFooter {...props} />);
    expect(wrapper).toMatchSnapshot();
  });

  it('testing showing no actions', () => {
    const wrapper = mount(<FormFooter />);
    expect(wrapper).toMatchSnapshot();
  });

  it('testing action click', () => {
    const action1 = {
      name: 'action1',
      execute: jest.fn()
    };

    const action2 = {
      name: 'action2',
      execute: jest.fn()
    };

    const props = {
      actions: [action1, action2]
    };

    const wrapper = mount(<FormFooter {...props} />);
    expect(wrapper).toMatchSnapshot();

    const button1 = wrapper.findWhere(node => node.key() === 'submit-action1');
    button1.simulate('click');

    expect(action1.execute).toBeCalledTimes(1);

    const button2 = wrapper.findWhere(node => node.key() === 'submit-action2');
    button2.simulate('click');

    expect(action2.execute).toBeCalledTimes(1);
  });
});
