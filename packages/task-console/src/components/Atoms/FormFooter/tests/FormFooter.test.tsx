import React from 'react';
import { mount } from 'enzyme';
import FormFooter from '../FormFooter';

describe('Form Footer test', () => {
  it('testing showing actions', () => {
    const props = {
      actions: [
        {
          name: 'action1',
          phase: 'action1',
          outputs: [],
          primary: true
        },
        {
          name: 'action2',
          phase: 'action2',
          outputs: ['output'],
          primary: false
        }
      ],
      onActionClick: jest.fn()
    };

    const wrapper = mount(<FormFooter {...props} />);
    expect(wrapper).toMatchSnapshot();
  });

  it('testing showing empty actions', () => {
    const props = {
      actions: [],
      onActionClick: jest.fn()
    };

    const wrapper = mount(<FormFooter {...props} />);
    expect(wrapper).toMatchSnapshot();
  });

  it('testing showing no actions', () => {
    const props = {
      onActionClick: jest.fn()
    };

    const wrapper = mount(<FormFooter {...props} />);
    expect(wrapper).toMatchSnapshot();
  });

  it('testing action click', () => {
    const action1 = {
      name: 'action1',
      phase: 'action1',
      outputs: [],
      primary: true
    };

    const action2 = {
      name: 'action2',
      phase: 'action2',
      outputs: ['output'],
      primary: false
    };

    const props = {
      actions: [action1, action2],
      onActionClick: jest.fn()
    };

    const wrapper = mount(<FormFooter {...props} />);
    expect(wrapper).toMatchSnapshot();

    const button1 = wrapper.findWhere(node => node.key() === 'submit-action1');
    button1.simulate('click');

    expect(props.onActionClick).toBeCalledTimes(1);
    expect(props.onActionClick).toBeCalledWith(action1);

    const button2 = wrapper.findWhere(node => node.key() === 'submit-action2');
    button2.simulate('click');

    expect(props.onActionClick).toBeCalledTimes(2);
    expect(props.onActionClick).toBeCalledWith(action2);
  });
});
