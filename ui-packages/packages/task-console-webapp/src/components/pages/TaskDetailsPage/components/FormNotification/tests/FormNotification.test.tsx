import React from 'react';
import FormNotification, { Notification } from '../FormNotification';
import {
  AlertActionCloseButton,
  AlertActionLink,
  Alert
} from '@patternfly/react-core';
import { mount } from 'enzyme';

describe('FormNotification test', () => {
  it('Simple notification', () => {
    const notificationProps: Notification = {
      type: 'success',
      message: 'The form has been submitted',
      close: jest.fn()
    };

    const wrapper = mount(
      <FormNotification notification={notificationProps} />
    ).find('FormNotification');

    expect(wrapper).toMatchSnapshot();

    const alert = wrapper.find(Alert);

    expect(alert.exists()).toBeTruthy();
    expect(alert.props().variant).toBe('success');

    expect(wrapper.html()).toContain(notificationProps.message);

    const button = wrapper.find(AlertActionCloseButton).find('button');

    button.simulate('click');

    expect(notificationProps.close).toBeCalled();
  });

  it('Notification with details', async () => {
    const notificationProps: Notification = {
      type: 'error',
      message: 'The form has been submitted',
      close: jest.fn(),
      details: 'The details here!'
    };

    let wrapper = mount(
      <FormNotification notification={notificationProps} />
    ).find('FormNotification');
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

  it('Notification with custom action', async () => {
    const notificationProps: Notification = {
      type: 'success',
      message: 'The form has been submitted',
      close: jest.fn(),
      customAction: {
        label: 'Custom action',
        onClick: jest.fn()
      }
    };

    const wrapper = mount(
      <FormNotification notification={notificationProps} />
    ).find('FormNotification');
    expect(wrapper).toMatchSnapshot();

    expect(wrapper.html()).toContain(notificationProps.message);

    const button = wrapper.find(AlertActionLink).find('button');

    expect(button.exists()).toBeTruthy();
    expect(button.getDOMNode().innerHTML).toBe('Custom action');

    button.simulate('click');

    expect(notificationProps.customAction.onClick).toBeCalled();
  });
});
