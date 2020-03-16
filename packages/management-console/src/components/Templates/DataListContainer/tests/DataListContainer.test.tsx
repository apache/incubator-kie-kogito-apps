import React from 'react';
import { shallow } from 'enzyme';
import DataListContainer from '../DataListContainer';
import { ProcessInstanceState } from '../../../../graphql/types';

const props = {
  arrayOfStates: [ProcessInstanceState.Active],
  setArrayOfStates: jest.fn()
};
describe('DataListContainer component tests', () => {
  it('Snapshot tests', () => {
    const wrapper = shallow(<DataListContainer {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
});
