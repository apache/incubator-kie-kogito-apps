import React from 'react';
import ExecutionStatus from '../ExecutionStatus';
import { shallow } from 'enzyme';

describe('Execution status', () => {
  test('renders a positive outcome', () => {
    const wrapper = shallow(<ExecutionStatus result={true} />);
    expect(wrapper).toMatchSnapshot();
  });

  test('renders a negative outcome', () => {
    const wrapper = shallow(<ExecutionStatus result={false} />);
    expect(wrapper).toMatchSnapshot();
  });
});
