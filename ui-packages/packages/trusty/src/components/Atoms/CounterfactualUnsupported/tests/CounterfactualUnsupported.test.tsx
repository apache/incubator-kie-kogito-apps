import React from 'react';
import { shallow } from 'enzyme';
import CounterfactualUnsupported from '../CounterfactualUnsupported';

describe('CounterfactualUnsupported status', () => {
  test('renders inputs status', () => {
    const wrapper = shallow(
      <CounterfactualUnsupported
        isAtLeastOneInputSupported={false}
        isAtLeastOneOutcomeSupported={true}
      />
    );

    expect(wrapper).toMatchSnapshot();
    const items = wrapper.find('ListItem');
    expect(items.length).toBe(1);
    expect(items.props()['data-ouia-component-id']).toBe('inputs-message');
  });

  test('renders outputs status', () => {
    const wrapper = shallow(
      <CounterfactualUnsupported
        isAtLeastOneInputSupported={true}
        isAtLeastOneOutcomeSupported={false}
      />
    );

    expect(wrapper).toMatchSnapshot();
    const items = wrapper.find('ListItem');
    expect(items.length).toBe(1);
    expect(items.props()['data-ouia-component-id']).toBe('outputs-message');
  });

  test('renders inputs and outputs status', () => {
    const wrapper = shallow(
      <CounterfactualUnsupported
        isAtLeastOneInputSupported={false}
        isAtLeastOneOutcomeSupported={false}
      />
    );

    expect(wrapper).toMatchSnapshot();
    const items = wrapper.find('ListItem');
    expect(items.length).toBe(2);
    expect(items.at(0).props()['data-ouia-component-id']).toBe(
      'inputs-message'
    );
    expect(items.at(1).props()['data-ouia-component-id']).toBe(
      'outputs-message'
    );
  });
});
