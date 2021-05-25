import React from 'react';
import { mount } from 'enzyme';
import CounterfactualAnalysis from '../CounterfactualAnalysis';
import { ItemObject, Outcome } from '../../../../types';

describe('CounterfactualAnalysis', () => {
  test('renders correctly', () => {
    const wrapper = mount(
      <CounterfactualAnalysis
        inputs={inputs}
        outcomes={outcomes}
        executionId={executionId}
        containerHeight={900}
        containerWidth={900}
      />
    );

    expect(wrapper).toMatchSnapshot();
  });

  test('has the correct initial state', () => {
    const wrapper = mount(
      <CounterfactualAnalysis
        inputs={inputs}
        outcomes={outcomes}
        executionId={executionId}
        containerHeight={900}
        containerWidth={900}
      />
    );

    expect(
      wrapper.find('Button.counterfactual-run').props()['isAriaDisabled']
    ).toBeTruthy();
    expect(wrapper.find('CounterfactualOutcomesSelected').text()).toMatch('');
    expect(wrapper.find('CounterfactualTable Thead Tr')).toHaveLength(1);
    expect(wrapper.find('CounterfactualTable Tbody Tr')).toHaveLength(1);
    expect(
      wrapper
        .find('CounterfactualTable Tbody Tr Td')
        .at(1)
        .text()
    ).toMatch('Credit Score');
  });

  test('handles input selection, constraints change and outcome selection', () => {
    const wrapper = mount(
      <CounterfactualAnalysis
        inputs={inputs}
        outcomes={outcomes}
        executionId={executionId}
        containerHeight={900}
        containerWidth={900}
      />
    );
    expect(wrapper.find('CounterfactualInputDomainEdit')).toHaveLength(0);
    expect(
      wrapper.find('Button.counterfactual-run').props()['isAriaDisabled']
    ).toBeTruthy();

    wrapper
      .find(
        'CounterfactualTable Tbody Tr:first-child Td:first-child SelectColumn'
      )
      .simulate('change');

    wrapper
      .find('CounterfactualTable Tbody Tr:first-child Td')
      .at(2)
      .find('Button')
      .simulate('click');

    expect(wrapper.find('CounterfactualInputDomainEdit')).toHaveLength(1);
    expect(wrapper.find('CounterfactualNumericalDomainEdit')).toHaveLength(1);

    const lowerBound = wrapper
      .find('CounterfactualNumericalDomainEdit SplitItem')
      .at(0)
      .find('input');
    lowerBound.getDOMNode<HTMLInputElement>().value = '1';
    lowerBound.simulate('change', '1');

    const upperBound = wrapper
      .find('CounterfactualNumericalDomainEdit SplitItem')
      .at(1)
      .find('input');
    upperBound.getDOMNode<HTMLInputElement>().value = '10';
    upperBound.simulate('change', '10');

    wrapper
      .find('CounterfactualInputDomainEdit ActionListItem:first-child Button')
      .simulate('click');

    expect(wrapper.find('CounterfactualInputDomainEdit')).toHaveLength(0);
    expect(
      wrapper
        .find('CounterfactualTable Tbody Tr:first-child Td')
        .at(2)
        .find('Button')
        .text()
    ).toMatch('1-10');

    expect(wrapper.find('CounterfactualOutcomeSelection')).toHaveLength(0);
    wrapper.find('Button.counterfactual-setup-outcomes').simulate('click');
    expect(wrapper.find('CounterfactualOutcomeSelection')).toHaveLength(1);
    wrapper
      .find('CounterfactualOutcomeSelection Touchspin Button')
      .at(1)
      .simulate('click');
    wrapper
      .find('CounterfactualOutcomeSelection ModalBoxFooter Button:first-child')
      .simulate('click');
    expect(wrapper.find('CounterfactualOutcomesSelected').text()).toMatch(
      'Selected Outcomes: Risk Score: 2'
    );
    expect(
      wrapper.find('Button.counterfactual-run').props()['isAriaDisabled']
    ).toBeFalsy();
  });
});

const inputs: ItemObject[] = [
  {
    components: null,
    name: 'Credit Score',
    kind: 'UNIT',
    typeRef: 'number',
    value: 738
  }
];
const outcomes: Outcome[] = [
  {
    evaluationStatus: 'SUCCEEDED',
    hasErrors: false,
    messages: [],
    outcomeId: '_c6e56793-68d0-4683-b34b-5e9d69e7d0d4',
    outcomeName: 'Risk Score',
    outcomeResult: {
      name: 'Risk Score',
      kind: 'UNIT',
      typeRef: 'number',
      value: 1,
      components: null
    }
  }
];
const executionId = 'b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000';
