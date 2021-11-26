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
import { act } from 'react-dom/test-utils';
import FormErrorsWrapper from '../FormErrorsWrapper';
import { EmptyState, Button, ClipboardCopy } from '@patternfly/react-core';

const MockedComponent = (): React.ReactElement => {
  return <></>;
};

jest.mock('@patternfly/react-core', () => ({
  ...jest.requireActual('@patternfly/react-core'),
  Button: () => <MockedComponent />,
  EmptyStateIcon: () => <MockedComponent />,
  ClipboardCopy: () => <MockedComponent />
}));

describe('FormErrorsWrapper tests', () => {
  it('Snapshot', () => {
    const error = new Error('Test error');

    let wrapper = mount(<FormErrorsWrapper error={error} />);

    expect(wrapper).toMatchSnapshot();

    const emptyState = wrapper.find(EmptyState);
    expect(emptyState.exists()).toBeTruthy();

    const button = wrapper.find(Button);
    expect(button.exists()).toBeTruthy();

    let clipboard = wrapper.find(ClipboardCopy);
    expect(clipboard.exists()).toBeFalsy();

    act(() => {
      button.props().onClick(undefined);
    });

    wrapper = wrapper.update();
    expect(wrapper).toMatchSnapshot();

    clipboard = wrapper.find(ClipboardCopy);

    expect(clipboard.exists()).toBeTruthy();
    expect(clipboard.props().isExpanded).toBeTruthy();
  });
});
