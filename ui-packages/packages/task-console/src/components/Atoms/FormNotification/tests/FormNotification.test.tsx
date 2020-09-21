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
import FormNotification, { Notification } from '../FormNotification';
import {
  AlertActionCloseButton,
  AlertActionLink,
  Alert
} from '@patternfly/react-core';
import { getWrapper } from '@kogito-apps/common';
import { NotificationType } from '../../../../util/Variants';

describe('FormNotification test', () => {
  it('Simple notification test', () => {
    const notificationProps: Notification = {
      type: NotificationType.SUCCESS,
      message: 'The form has been submitted',
      close: jest.fn()
    };

    const wrapper = getWrapper(
      <FormNotification notification={notificationProps} />,
      'FormNotification'
    );

    expect(wrapper).toMatchSnapshot();

    const alert = wrapper.find(Alert);

    expect(alert.exists()).toBeTruthy();
    expect(alert.props().variant).toBe('success');

    expect(wrapper.html()).toContain(notificationProps.message);

    const button = wrapper.find(AlertActionCloseButton).find('button');

    button.simulate('click');

    expect(notificationProps.close).toBeCalled();
  });

  it('Notification with details test', async () => {
    const notificationProps: Notification = {
      type: NotificationType.ERROR,
      message: 'The form has been submitted',
      close: jest.fn(),
      details: 'The details here!'
    };

    let wrapper = getWrapper(
      <FormNotification notification={notificationProps} />,
      'FormNotification'
    );
    expect(wrapper).toMatchSnapshot();

    expect(wrapper.html()).toContain(notificationProps.message);

    const alert = wrapper.find(Alert);

    expect(alert.exists()).toBeTruthy();
    expect(alert.props().variant).toBe('danger');

    const button = wrapper.find(AlertActionLink).find('button');

    expect(button.exists()).toBeTruthy();
    expect(button.getDOMNode().innerHTML).toBe('View details');

    button.simulate('click');

    expect(wrapper).toMatchSnapshot();

    wrapper = wrapper.update().find('FormNotification');

    expect(wrapper.html()).toContain(notificationProps.details);
  });

  it('Notification with custom action test', async () => {
    const notificationProps: Notification = {
      type: NotificationType.SUCCESS,
      message: 'The form has been submitted',
      close: jest.fn(),
      customAction: {
        label: 'Custom action',
        onClick: jest.fn()
      }
    };

    const wrapper = getWrapper(
      <FormNotification notification={notificationProps} />,
      'FormNotification'
    );
    expect(wrapper).toMatchSnapshot();

    expect(wrapper.html()).toContain(notificationProps.message);

    const button = wrapper.find(AlertActionLink).find('button');

    expect(button.exists()).toBeTruthy();
    expect(button.getDOMNode().innerHTML).toBe('Custom action');

    button.simulate('click');

    expect(notificationProps.customAction.onClick).toBeCalled();
  });
});
