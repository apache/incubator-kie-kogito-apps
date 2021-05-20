import React from 'react';
import { mount } from 'enzyme';
import CounterfactualAnalysis from '../CounterfactualAnalysis';
import { ItemObject, Outcome } from '../../../../types';
import useCFTableSizes from '../../CounterfactualTable/useCFTableSizes';

jest.mock('../../CounterfactualTable/useCFTableSizes');

describe('CounterfactualAnalysis', () => {
  test('renders correctly', () => {
    (useCFTableSizes as jest.Mock).mockReturnValue(CFSizes);

    const wrapper = mount(
      <CounterfactualAnalysis
        inputs={inputs}
        outcomes={outcomes}
        executionId={executionId}
      />
    );

    expect(wrapper).toMatchSnapshot();
  });

  test('has the correct initial state', () => {
    (useCFTableSizes as jest.Mock).mockReturnValue(CFSizes);

    const wrapper = mount(
      <CounterfactualAnalysis
        inputs={inputs}
        outcomes={outcomes}
        executionId={executionId}
      />
    );

    expect(
      wrapper.find('Button.counterfactual-run').props()['isAriaDisabled']
    ).toBeTruthy();
    expect(wrapper.find('CounterfactualTable Thead Tr')).toHaveLength(1);
    expect(wrapper.find('CounterfactualTable Tbody Tr')).toHaveLength(1);
    expect(
      wrapper
        .find('CounterfactualTable Tbody Tr Td')
        .at(1)
        .text()
    ).toMatch('Credit Score');
    expect(wrapper.find('CounterfactualInputDomainEdit')).toHaveLength(0);
    wrapper
      .find('button.counterfactual-constraint-edit')
      .at(0)
      .simulate('click');
    expect(wrapper.find('CounterfactualInputDomainEdit')).toHaveLength(1);
  });
});

const inputs: ItemObject[] = [
  { components: null, name: 'Credit Score', typeRef: 'number', value: 738 }
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
      typeRef: 'number',
      value: 1,
      components: null
    }
  }
];
const executionId = 'b2b0ed8d-c1e2-46b5-3ac54ff4beae-1000';
const CFSizes = {
  containerWidth: 500,
  containerHeight: 500,
  windowSize: 900
};
