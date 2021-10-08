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
    const items = wrapper.find('EmptyStateBody p');
    expect(items.length).toBe(3);
    expect(items.get(1).props['data-ouia-component-id']).toBe('message-inputs');
  });

  test('renders outputs status', () => {
    const wrapper = shallow(
      <CounterfactualUnsupported
        isAtLeastOneInputSupported={true}
        isAtLeastOneOutcomeSupported={false}
      />
    );

    expect(wrapper).toMatchSnapshot();
    const items = wrapper.find('EmptyStateBody p');
    expect(items.length).toBe(3);
    expect(items.get(1).props['data-ouia-component-id']).toBe(
      'message-outcomes'
    );
  });

  test('renders inputs and outputs status', () => {
    const wrapper = shallow(
      <CounterfactualUnsupported
        isAtLeastOneInputSupported={false}
        isAtLeastOneOutcomeSupported={false}
      />
    );

    expect(wrapper).toMatchSnapshot();
    const items = wrapper.find('EmptyStateBody p');
    expect(items.length).toBe(3);
    expect(items.get(1).props['data-ouia-component-id']).toBe(
      'message-inputs-outcomes'
    );
  });
});
